package com.example.vndbapp.data.local.repository

import com.example.vndbapp.data.local.dao.VisualNovelDao
import com.example.vndbapp.data.local.entity.VisualNovelEntity
import com.example.vndbapp.data.remote.api.VisualNovelApiService
import com.example.vndbapp.data.mapper.toEntity
import com.example.vndbapp.data.model.RequestBodyVisualNovel
import com.example.vndbapp.data.model.VisualNovel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalVisualNovelRepositoryImpl @Inject constructor(
    private val visualNovelDao: VisualNovelDao,
    private val visualNovelApiService: VisualNovelApiService
): LocalVisualNovelRepository {
    override suspend fun getVisualNovelsByPage(
        page: Int,
        fields: String,
        filters: List<String>
    ): Flow<List<VisualNovelEntity>> {

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
                visualNovelDao.insertVisualNovels(vns = entities)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return visualNovelDao.getVisualNovelsByPage(page = page)
    }

    override suspend fun saveVisualNovels(vns: List<VisualNovel>, page: Int) {
        val entities = vns.map { it.toEntity(page) }
        visualNovelDao.insertVisualNovels(entities)
    }
}