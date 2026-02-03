package com.example.vndbapp.datalayer.api.repository

import com.example.vndbapp.model.VisualNovelRespone
import retrofit2.Response

/**
 * INTERVIEW EXPLANATION - Repository Interface Design:
 *
 * === OPTION 1: FULLY PARAMETERIZED (CURRENT) ===
 *
 * This design gives maximum flexibility. The caller decides:
 * - What fields to fetch (optimizes payload size)
 * - What filters to apply (dynamic search)
 *
 * INTERVIEW TALKING POINT:
 * "I parameterized fields and filters because different screens need different data.
 * The list screen only needs titles for performance, while the detail screen needs
 * images and descriptions. By passing these as parameters, we minimize network
 * usage and give the UI full control over what to request."
 *
 * === ALTERNATIVE: OPTION 2 - SPECIFIC METHODS ===
 *
 * If parameterized feels too "leaky", create intent-revealing methods:
 *
 * interface VisualNovelRepository {
 *     // Most common: simple list with titles
 *     suspend fun getVisualNovels(page: Int): Response<VisualNovelRespone>
 *
 *     // List with images
 *     suspend fun getVisualNovelsWithImages(page: Int): Response<VisualNovelRespone>
 *
 *     // Search by title
 *     suspend fun searchByTitle(query: String, page: Int): Response<VisualNovelRespone>
 *
 *     // Get full details by ID
 *     suspend fun getById(id: String): Response<VisualNovelRespone>
 * }
 *
 * PROS: Self-documenting, simple for common cases
 * CONS: Interface grows with each new use case
 *
 * === ALTERNATIVE: OPTION 3 - TYPE-SAFE BUILDER ===
 *
 * Create domain models that map to API structures:
 *
 * data class VisualNovelRequest(
 *     val fields: Set<VisualNovelField>,
 *     val page: Int,
 *     val filters: List<VisualNovelFilter>
 * )
 *
 * enum class VisualNovelField { TITLE, DESCRIPTION, IMAGE, RATING }
 * sealed class VisualNovelFilter { ... }
 *
 * PROS: Type-safe, compiler checks validity
 * CONS: More code, may not map 1:1 to API
 */
interface VisualNovelRepository {
    /**
     * Fetches visual novels with flexible field selection and filtering.
     *
     * @param page Page number for pagination
     * @param fields Comma-separated fields (e.g., "title,image.url").
     *               Defaults to "title" for lightweight list display.
     * @param filters List of filter strings in VNDB API format.
     *                e.g., ["title", "~", "Fate"] for title contains "Fate"
     */
    suspend fun getVisualNovels(
        page: Int,
        fields: String = "title",
        filters: List<String> = emptyList()
    ): Response<VisualNovelRespone>
}
