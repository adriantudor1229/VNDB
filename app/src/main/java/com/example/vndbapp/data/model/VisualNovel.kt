package com.example.vndbapp.data.model

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class VisualNovel(
    val title: String,
    val image: Image,
    val id: String,
    val description: String
)

data class RequestBodyVisualNovel(
    val fields: String,
    val page: Int,
    val filters: List<String>
)


data class VisualNovelResponse(
    val results: List<VisualNovel>,
    val more: Boolean
)

@Serializable
data class Image(
    val url: String? = null,
    val thumbnail: String? = null,
    @param:Json(name = "sexual")
    val explicit: Double
)
