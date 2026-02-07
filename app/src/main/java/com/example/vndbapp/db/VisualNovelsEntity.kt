package com.example.vndbapp.db

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vndbapp.model.Image
import com.example.vndbapp.model.VisualNovel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Entity(tableName = "visual_novels")
data class VisualNovelsEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,

    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

fun VisualNovel.toEntity(): VisualNovelsEntity {
    return VisualNovelsEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = image.url,
        thumbnailUrl = image.thumbnail
    )
}

fun VisualNovelsEntity.toModel(): VisualNovel {
    return VisualNovel(
        id = id,
        title = title,
        description = description,
        image = Image(
            url = imageUrl,
            thumbnail = thumbnailUrl
        )
    )
}

@Dao
interface VisualNovelDao {

    @Query("SELECT * FROM visual_novels ORDER BY created_at DESC")
    fun getAllVisualNovels(): Flow<List<VisualNovelsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisualNovels(novels: List<VisualNovelsEntity>)

}

@Database(
    entities = [VisualNovelsEntity::class],
    version = 1,
    exportSchema = true
)

abstract class VisualNovelDatabase : RoomDatabase() {
    abstract fun visualNovelDao(): VisualNovelDao

    companion object {
        const val DATABASE_NAME = "vndb_database"
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideVndbDatabase(
        @ApplicationContext context: Context
    ): VisualNovelDatabase {
        return Room.databaseBuilder(
            context,
            VisualNovelDatabase::class.java,
            VisualNovelDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(false) // For development - remove in production
            // .addMigrations(Migration1To2)  // Uncomment for production migrations
            .build()
    }

    @Provides
    @Singleton
    fun provideVisualNovelDao(
        database: VisualNovelDatabase
    ): VisualNovelDao {
        return database.visualNovelDao()
    }
}

@Singleton
class LocalVisualNovelRepository @Inject constructor(
    private val visualNovelDao: VisualNovelDao
) {
    suspend fun getAllVisualNovels(): Flow<List<VisualNovel>> {
        return visualNovelDao.getAllVisualNovels()
            .map { entities ->  entities.map { entity -> entity.toModel() } }
    }

    suspend fun saveVisualNovels(novels: List<VisualNovel>) {
        val entities = novels.map { it.toEntity() }
        visualNovelDao.insertVisualNovels(entities)
    }
}