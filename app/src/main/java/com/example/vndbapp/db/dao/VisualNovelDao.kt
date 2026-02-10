package com.example.vndbapp.db.dao

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.vndbapp.db.VisualNovelsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisualNovelDao {

    @Query("SELECT * FROM visual_novels WHERE page = :page")
    fun getVisualNovelsByPage(page: Int): Flow<List<VisualNovelsEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisualNovels(novels: List<VisualNovelsEntity>)

    @Query("SELECT COUNT(*) FROM visual_novels WHERE page = :page")
    suspend fun getPageCount(page: Int): Int
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