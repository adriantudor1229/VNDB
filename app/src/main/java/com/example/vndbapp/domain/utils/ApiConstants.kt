package com.example.vndbapp.domain.utils

object ApiConstants {
    const val FIELDS = "title, image.url, image.thumbnail, image.sexual, description"
    const val CHARACTER_FIELDS = "name, original, description, image.url, image.sexual, vns.id, vns.role, vns.spoiler"
    const val BASE_URL = "https://api.vndb.org/kana/"
}
