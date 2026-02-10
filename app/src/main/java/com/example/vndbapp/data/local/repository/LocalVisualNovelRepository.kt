package com.example.vndbapp.data.local.repository

import com.example.vndbapp.data.local.entity.VisualNovelEntity
import com.example.vndbapp.data.model.VisualNovel
import kotlinx.coroutines.flow.Flow

interface LocalVisualNovelRepository {

    suspend fun getVisualNovelsByPage(
        page: Int,
    ): Flow<List<VisualNovelEntity>>

    suspend fun saveVisualNovels(
        vns: List<VisualNovel>,
        page: Int
    )
}
