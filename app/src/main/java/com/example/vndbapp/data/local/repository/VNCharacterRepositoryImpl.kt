package com.example.vndbapp.data.local.repository

import com.example.vndbapp.data.local.dao.CharacterDao
import com.example.vndbapp.data.local.entity.CharacterEntity
import com.example.vndbapp.data.mapper.toEntity
import com.example.vndbapp.data.model.RequestBodyCharacterByVn
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.remote.api.VisualNovelApiService
import com.example.vndbapp.domain.repository.CharacterRepository
import com.example.vndbapp.domain.utils.ApiConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class VNCharacterRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao,
    private val apiService: VisualNovelApiService,
) : CharacterRepository {

    override fun getCharactersForVn(vnId: String): Flow<Resource<List<CharacterEntity>>> =
        flow {
            emit(value = Resource.Loading)

            val cachedCount = characterDao.getCountByVnId(vnId)

            if (cachedCount == 0) {
                try {
                    val filterString = """["vn", "=", ["id", "=", "$vnId"]]"""

                    val body = RequestBodyCharacterByVn(
                        fields = ApiConstants.CHARACTER_FIELDS,
                        filters = filterString,
                        results = 50,
                    )
                    val response = apiService.getCharactersByVn(requestBody = body)
                    if (response.isSuccessful && response.body() != null) {
                        val results = response.body()!!.results
                        val entities = results.map { character ->
                            val role = character.vns
                                .firstOrNull {it.id == vnId}
                                ?.role?.name
                            character.toEntity(vnId = vnId, role = role)
                        }
                        characterDao.insertCharacters(characters = entities)
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
            val data = characterDao.getCharactersByVnId(vnId).first()
            emit(value = Resource.Success(data))
        }.flowOn(context = Dispatchers.IO)
}
