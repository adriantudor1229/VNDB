package com.example.vndbapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vndbapp.data.local.entity.VisualNovelEntity
import com.example.vndbapp.data.local.repository.LocalVisualNovelRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VisualNovelViewModel @Inject constructor(
    private val localVisualNovelRepositoryImpl: LocalVisualNovelRepositoryImpl
) : ViewModel() {
    private val _currentPage = MutableStateFlow(0)

    val currentPageVns: StateFlow<List<VisualNovelEntity>> =
        _currentPage.flatMapLatest { page ->
            localVisualNovelRepositoryImpl.getVisualNovelsByPage(
                page = page
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadPage(
        page: Int,
    ) {
        _currentPage.value = page
    }
}
