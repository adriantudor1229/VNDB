package com.example.vndbapp.data.remote.api

import com.example.vndbapp.data.model.CharacterResponse
import com.example.vndbapp.data.model.RequestBodyCharacterByVn
import com.example.vndbapp.data.model.RequestBodyVisualNovel
import com.example.vndbapp.data.model.RequestBodyVisualNovelCharacter
import com.example.vndbapp.data.model.VisualNovelResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface VisualNovelApiService {
    // Since this is our format we can see that we need to specify header
    // curl https://api.vndb.org/kana/vn --header 'Content-Type: application/json' --data '{
    //    "filters": ["id", "=", "v17"],
    //    "fields": "title, image.url" }'

    @Headers("Content-Type: application/json")
    @POST("vn")
    suspend fun getVisualNovels(
        @Body requestBodyVisualNovel: RequestBodyVisualNovel,
    ): Response<VisualNovelResponse>

    @Headers("Content-Type: application/json")
    @POST(value = "character")
    suspend fun getCharacters(
        @Body requestBodyVisualNovel: RequestBodyVisualNovelCharacter,
    ): Response<VisualNovelResponse>

    @Headers("Content-Type: application/json")
    @POST(value = "character")
    suspend fun getCharactersByVn(
        @Body requestBody: RequestBodyCharacterByVn,
    ): Response<CharacterResponse>
}
