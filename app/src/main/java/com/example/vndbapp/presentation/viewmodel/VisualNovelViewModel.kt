package com.example.vndbapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vndbapp.data.local.repository.LocalVisualNovelRepositoryImpl
import com.example.vndbapp.data.local.entity.VisualNovelEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VisualNovelViewModel @Inject constructor(
    private val localVisualNovelRepositoryImpl: LocalVisualNovelRepositoryImpl
) : ViewModel() {
    private val _fields = MutableStateFlow("title, image.url, image.thumbnail, description")
    private val _filters = MutableStateFlow<List<String>>(emptyList())
    private val _currentPage = MutableStateFlow(0)

    val currentPageVns: StateFlow<List<VisualNovelEntity>> = combine(
        _currentPage,
        _fields,
        _filters
    ) { page, fields, filters ->
        Triple(page, fields, filters)
    }.flatMapLatest { (page, fields, filters) ->
        localVisualNovelRepositoryImpl.getVisualNovelsByPage(
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
