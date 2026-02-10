package com.example.vndbapp.presentation.viewmodel

sealed interface VisualNovelEvent {
    data class GetVisualNovel(
        val page: Int,
        val fields: String = "title, image.url, image.thumbnail, description",
        val filters: List<String> = emptyList()
    ) : VisualNovelEvent
}
