package com.example.vndbapp.data.model

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class VisualNovel(
    val title: String,
    val image: Image,
    val id: String,
    val description: String,
)

data class RequestBodyVisualNovel(
    val fields: String,
    val page: Int,
    val filters: List<String>,
)

data class RequestBodyVisualNovelCharacter(
    val fields: String,
    val filters: List<Any>,
    val result: Int
)

data class RequestBodyCharacterByVn(
    val fields: String,
    val filters: String,
    val results: Int,
)

data class VisualNovelResponse(
    val results: List<VisualNovel>,
    val more: Boolean,
)

@Serializable
data class Image(
    val url: String? = null,
    val thumbnail: String? = null,
    @param:Json(name = "sexual")
    val explicit: Double,
)

// --- Character models ---

@Serializable
data class VNCharacter(
    val id: String,
    val name: String,
    val original: String? = null,
    val image: VNCharacterImage? = null,
    val description: String? = null,
)

@Serializable
data class VNCharacterImage(
    val url: String,
    val sexual: Double = 0.0,
)

data class CharacterResponse(
    val results: List<VNCharacter>,
    val more: Boolean,
)

data class RequestBodyCharacter(
    val fields: String,
    val page: Int,
    val filters: List<String>,
)