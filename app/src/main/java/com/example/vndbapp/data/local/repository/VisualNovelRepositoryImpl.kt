package com.example.vndbapp.data.local.repository

import com.example.vndbapp.data.local.dao.VisualNovelDao
import com.example.vndbapp.data.local.entity.VisualNovelEntity
import com.example.vndbapp.data.mapper.toEntity
import com.example.vndbapp.data.model.RequestBodyVisualNovel
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.remote.api.VisualNovelApiService
import com.example.vndbapp.domain.repository.VisualNovelRepository
import com.example.vndbapp.domain.utils.ApiConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class VisualNovelRepositoryImpl
    @Inject
    constructor(
        private val visualNovelDao: VisualNovelDao,
        private val visualNovelApiService: VisualNovelApiService,
    ) : VisualNovelRepository {
        override fun getVisualNovelsByPage(page: Int): Flow<Resource<List<VisualNovelEntity>>> =
            flow {
                emit(value = Resource.Loading)

                val cachedCount = visualNovelDao.getPageCount(page)

                if (cachedCount == 0) {
                    try {
                        val response =
                            visualNovelApiService.getVisualNovels(
                                requestBodyVisualNovel =
                                    RequestBodyVisualNovel(
                                        page = page,
                                        fields = ApiConstants.FIELDS,
                                        filters = emptyList(),
                                    ),
                            )
                        if (response.isSuccessful && response.body() != null) {
                            val results = response.body()!!.results
                            val entities = results.map { it.toEntity(page = page) }
                            visualNovelDao.insertVisualNovels(vns = entities)
                        } else {
                            val errorMessage = "API Error: ${response.code()} - ${response.message()}"
                            emit(Resource.Error(errorMessage))
                            return@flow
                        }
                    } catch (e: IOException) {
                        emit(Resource.Error(message = "Network error: ${e.message}", cause = e))
                        return@flow
                    } catch (e: HttpException) {
                        emit(Resource.Error(message = "HTTP error: ${e.message}", cause = e))
                        return@flow
                    }
                }
                val data = visualNovelDao.getVisualNovelsByPage(page = page).first()
                emit(value = Resource.Success(data))
            }.flowOn(context = Dispatchers.IO)
    }
