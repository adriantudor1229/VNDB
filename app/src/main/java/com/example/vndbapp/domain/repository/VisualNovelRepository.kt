package com.example.vndbapp.domain.repository

import com.example.vndbapp.data.local.entity.VisualNovelEntity
import com.example.vndbapp.data.model.Resource
import kotlinx.coroutines.flow.Flow

interface VisualNovelRepository {
    fun getVisualNovelsByPage(page: Int): Flow<Resource<List<VisualNovelEntity>>>
}
