package com.example.vndbapp.data.remote.api

import com.example.vndbapp.data.model.CharacterResponse
import com.example.vndbapp.data.model.RequestBodyCharacterByVn
import com.example.vndbapp.data.model.RequestBodyVisualNovel
import com.example.vndbapp.data.model.VisualNovelResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface VisualNovelApiService {
    @Headers("Content-Type: application/json")
    @POST("vn")
    suspend fun getVisualNovels(
        @Body requestBodyVisualNovel: RequestBodyVisualNovel,
    ): Response<VisualNovelResponse>

    @Headers("Content-Type: application/json")
    @POST(value = "character")
    suspend fun getCharactersByVn(
        @Body requestBody: RequestBodyCharacterByVn,
    ): Response<CharacterResponse>
}
