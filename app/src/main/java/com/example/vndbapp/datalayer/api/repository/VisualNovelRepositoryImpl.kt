package com.example.vndbapp.datalayer.api.repository

import com.example.vndbapp.datalayer.api.VisualNovelApiService
import com.example.vndbapp.model.RequestBodyVisualNovel
import com.example.vndbapp.model.VisualNovelRespone
import retrofit2.Response
import javax.inject.Inject

/**
 * INTERVIEW EXPLANATION - How This Functions:
 *
 * === SOLUTION: DEFAULT PARAMETERS FOR FLEXIBILITY ===
 *
 * By using default parameter values, we get the best of both worlds:
 * 1. Simple calls work with sensible defaults (just pass page)
 * 2. Complex calls can override fields and filters as needed
 *
 * INTERVIEW TALKING POINTS:
 * "I used default parameters instead of overloading because:
 * - One method instead of 4+ overloads (getVisualNovels, getVisualNovelsWithFields, etc.)
 * - Callers can specify only what they need
 * - Adding new parameters doesn't break existing calls
 * - Kotlin's named parameters make calls readable"
 *
 **/

class VisualNovelRepositoryImpl @Inject constructor(
    private val visualNovelApiService: VisualNovelApiService
) : VisualNovelRepository {

    override suspend fun getVisualNovels(
        page: Int,
        fields: String,
        filters: List<String>
    ): Response<VisualNovelRespone> {
        return visualNovelApiService.getVisualNovels(
            requestBodyVisualNovel = RequestBodyVisualNovel(
                fields = fields,
                page = page,
                filters = emptyList()
            )
        )
    }
}
