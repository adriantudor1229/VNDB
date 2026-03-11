package com.example.vndbapp.di

import android.content.Context
import androidx.room.Room
import com.example.vndbapp.data.local.dao.CharacterDao
import com.example.vndbapp.data.local.dao.VisualNovelDao
import com.example.vndbapp.data.local.database.VisualNovelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideVndbDatabase(
        @ApplicationContext context: Context,
    ): VisualNovelDatabase {
        return Room.databaseBuilder(
            context,
            VisualNovelDatabase::class.java,
            VisualNovelDatabase.DATABASE_NAME,
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideVisualNovelDao(database: VisualNovelDatabase): VisualNovelDao {
        return database.visualNovelDao()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: VisualNovelDatabase): CharacterDao {
        return database.characterDao()
    }
}
