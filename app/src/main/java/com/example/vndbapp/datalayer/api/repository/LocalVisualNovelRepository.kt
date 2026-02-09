package com.example.vndbapp.datalayer.api.repository

import com.example.vndbapp.db.dao.VisualNovelDao
import com.example.vndbapp.mapper.toEntity
import com.example.vndbapp.mapper.toModel
import com.example.vndbapp.model.VisualNovel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalVisualNovelRepository @Inject constructor(
    private val visualNovelDao: VisualNovelDao
) {
    fun getAllVisualNovels(): Flow<List<VisualNovel>> {
        return visualNovelDao.getAllVisualNovels()
            .map { entities -> entities.map { entity -> entity.toModel() } }
    }

    suspend fun saveVisualNovels(novels: List<VisualNovel>) {
        val entities = novels.map { it.toEntity() }
        visualNovelDao.insertVisualNovels(entities)
    }
}
