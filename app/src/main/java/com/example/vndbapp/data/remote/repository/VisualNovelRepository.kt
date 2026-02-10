package com.example.vndbapp.data.remote.repository

import com.example.vndbapp.data.model.VisualNovelResponse
import retrofit2.Response
interface VisualNovelRepository {
    suspend fun getVisualNovels(
        page: Int,
        fields: String = "title",
        filters: List<String> = emptyList()
    ): Response<VisualNovelResponse>
}
