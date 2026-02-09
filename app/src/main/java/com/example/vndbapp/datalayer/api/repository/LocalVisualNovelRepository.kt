package com.example.vndbapp.datalayer.api.repository

import com.example.vndbapp.datalayer.api.VisualNovelApiService
import com.example.vndbapp.db.VisualNovelsEntity
import com.example.vndbapp.db.dao.VisualNovelDao
import com.example.vndbapp.mapper.toEntity
import com.example.vndbapp.model.RequestBodyVisualNovel
import com.example.vndbapp.model.VisualNovel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalVisualNovelRepository @Inject constructor(
    private val visualNovelDao: VisualNovelDao,
    private val visualNovelApiService: VisualNovelApiService
) {
    suspend fun getVisualNovelsByPage(
        page: Int,
        fields: String,
        filters: List<String>
    ): Flow<List<VisualNovelsEntity>> {

        val cachedCount = visualNovelDao.getPageCount(page)

        if (cachedCount == 0) {
            try {
                val vns = visualNovelApiService.getVisualNovels(
                    requestBodyVisualNovel = RequestBodyVisualNovel(
                        fields = fields,
                        page = page,
                        filters = filters
                    )
                )
                val entities = vns.body()!!.results.map { it.toEntity(page = page) }
                visualNovelDao.insertVisualNovels(novels = entities)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return visualNovelDao.getVisualNovelsByPage(page = page)
    }

    suspend fun saveVisualNovels(novels: List<VisualNovel>, page: Int) {
        val entities = novels.map { it.toEntity(page) }
        visualNovelDao.insertVisualNovels(entities)
    }
}
