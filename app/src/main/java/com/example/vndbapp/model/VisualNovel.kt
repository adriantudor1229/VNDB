package com.example.vndbapp.model

data class VisualNovel(
    val title: String,
    val image: Image,
    val id: String
)

data class RequestBodyVisualNovel(
    val fields: String,
    val page: Int,
    val filters: List<String>  // was "filter", API expects "filters"
)

data class VisualNovelRespone(
    val results: List<VisualNovel>,  // API returns "results" not "result"
    val more: Boolean
)

data class Image(
    val url: String? = null,
    val thumbnail: String? = null
)