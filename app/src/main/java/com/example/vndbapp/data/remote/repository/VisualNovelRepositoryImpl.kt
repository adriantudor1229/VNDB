package com.example.vndbapp.data.remote.repository

import com.example.vndbapp.data.remote.api.VisualNovelApiService
import com.example.vndbapp.data.model.RequestBodyVisualNovel
import com.example.vndbapp.data.model.VisualNovelResponse
import retrofit2.Response
import javax.inject.Inject

class VisualNovelRepositoryImpl @Inject constructor(
    private val visualNovelApiService: VisualNovelApiService
) : VisualNovelRepository {

    override suspend fun getVisualNovels(
        page: Int,
        fields: String,
        filters: List<String>
    ): Response<VisualNovelResponse> {
        return visualNovelApiService.getVisualNovels(
            requestBodyVisualNovel = RequestBodyVisualNovel(
                fields = fields,
                page = page,
                filters = filters
            )
        )
    }
}
