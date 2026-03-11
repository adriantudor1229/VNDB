package com.example.vndbapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.domain.usecase.GetVisualNovelsByPageUseCase
import com.example.vndbapp.domain.utils.PresentationConstants
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
class VisualNovelViewModel
    @Inject
    constructor(
        private val getVisualNovelsByPageUseCase: GetVisualNovelsByPageUseCase,
    ) : ViewModel() {
        private val _currentPage = MutableStateFlow(value = 0)

        val currentPageVns: StateFlow<Resource<List<VisualNovel>>> =
            _currentPage.flatMapLatest { page ->
                getVisualNovelsByPageUseCase(page = page)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = PresentationConstants.STATE_RETENTION_TIMEOUT_MS),
                initialValue = Resource.Loading,
            )

        fun loadPage(page: Int) {
            _currentPage.value = page
        }
    }
