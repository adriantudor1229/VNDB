package com.example.vndbapp.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vndbapp.datalayer.api.repository.LocalVisualNovelRepository
import com.example.vndbapp.datalayer.api.repository.VisualNovelRepository
import com.example.vndbapp.db.VisualNovelsEntity
import com.example.vndbapp.model.VisualNovel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisualNovelViewModel @Inject constructor(
    private val localVisualNovelRepository: LocalVisualNovelRepository
) : ViewModel() {
    private val _fields = MutableStateFlow("title, image.url, image.thumbnail, description")
    private val _filters = MutableStateFlow<List<String>>(emptyList())
    private val _currentPage = MutableStateFlow(0)

    val currentPageVns: StateFlow<List<VisualNovelsEntity>> = combine(
        _currentPage,
        _fields,
        _filters
    ) { page, fields, filters ->
        Triple(page, fields, filters)
    }.flatMapLatest { (page, fields, filters) ->
        localVisualNovelRepository.getVisualNovelsByPage(
            page = page,
            fields = fields,
            filters = filters
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    init {
        onEvent(VisualNovelEvent.GetVisualNovel(page = 0))
    }

    fun onEvent(event: VisualNovelEvent) {
        when (event) {
            is VisualNovelEvent.GetVisualNovel -> {
                _currentPage.value = event.page
                _fields.value = event.fields
                _filters.value = event.filters
            }
        }
    }

    fun loadPage(
        page: Int,
        fields: String = "title, image.url, image.thumbnail, description",
        filters: List<String> = emptyList()
    ) {
        _currentPage.value = page
        _fields.value = fields
        _filters.value = filters
    }

}

sealed class VisualNovelState {
    object Loading : VisualNovelState()
    data class Success(val visualNovels: List<VisualNovel>) : VisualNovelState()
    data class Error(val message: String) : VisualNovelState()
}

sealed interface VisualNovelEvent {
    data class GetVisualNovel(
        val page: Int,
        val fields: String = "title, image.url, image.thumbnail, description",
        val filters: List<String> = emptyList()
    ) : VisualNovelEvent
}