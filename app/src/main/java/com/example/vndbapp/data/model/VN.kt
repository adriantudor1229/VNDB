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

@Serializable
data class VNCharacter(
    val id: String,
    val name: String,
    val original: String? = null,
    val image: VNCharacterImage? = null,
    val description: String? = null,
    @Json(name = "vns")
    val vns: List<CharacterVn> = emptyList(),
    val role: String? = null

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


enum class CharacterRole {
    @Json(name = "main")    MAIN,
    @Json(name = "primary") PRIMARY,
    @Json(name = "side")    SIDE,
    @Json(name = "appears") APPEARS
}

@Serializable
data class CharacterVn(
    @Json(name = "id")
    val id: String,                 // vndbid, e.g. "v17"

    @Json(name = "spoiler")
    val spoiler: Int,               // 0, 1, or 2

    @Json(name = "role")
    val role: CharacterRole,

    @Json(name = "release")
    val release: Release? = null    // Specific release, usually null
)

@Serializable
data class Release(
    @Json(name = "id")
    val id: String
)