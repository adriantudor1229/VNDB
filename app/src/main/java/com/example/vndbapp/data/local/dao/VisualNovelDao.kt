package com.example.vndbapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vndbapp.data.local.entity.VisualNovelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisualNovelDao {
    @Query("SELECT * FROM visual_novels WHERE page = :page")
    fun getVisualNovelsByPage(page: Int): Flow<List<VisualNovelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisualNovels(vns: List<VisualNovelEntity>)

    @Query("SELECT COUNT(*) FROM visual_novels WHERE page = :page")
    suspend fun getPageCount(page: Int): Int
}
