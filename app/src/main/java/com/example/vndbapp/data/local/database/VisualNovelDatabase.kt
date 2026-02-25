package com.example.vndbapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vndbapp.data.local.dao.VisualNovelDao
import com.example.vndbapp.data.local.entity.VisualNovelEntity

@Database(
    entities = [VisualNovelEntity::class],
    version = 1,
    exportSchema = false
)

abstract class VisualNovelDatabase : RoomDatabase() {
    abstract fun visualNovelDao(): VisualNovelDao

    companion object {
        const val DATABASE_NAME = "vndb_database"
    }
}
