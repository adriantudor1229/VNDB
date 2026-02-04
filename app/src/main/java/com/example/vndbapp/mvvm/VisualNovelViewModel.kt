package com.example.vndbapp.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vndbapp.datalayer.api.repository.VisualNovelRepository
import com.example.vndbapp.model.VisualNovel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisualNovelViewModel @Inject constructor(
    private val visualNovelRepository: VisualNovelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VisualNovelState>(VisualNovelState.Loading)
    val uiState: StateFlow<VisualNovelState> = _uiState.asStateFlow()

    fun onEvent(event: VisualNovelEvent) {
        when (event) {
            is VisualNovelEvent.GetVisualNovel -> getVisualNovels(
                page = event.page,
                title = event.title,
                visualNovels = event.filters,
            )
        }
    }

    private fun getVisualNovels(
        page: Int,
        title: String,
        visualNovels: List<String> = emptyList()
    ) {
        _uiState.value = VisualNovelState.Loading
        viewModelScope.launch {
            val response = visualNovelRepository.getVisualNovels(
                page = page,
                fields = title,
                filters = visualNovels
            )

            if (response.isSuccessful && response.body() != null) {
                val results = response.body()!!.results

                _uiState.update {
                    VisualNovelState.Success(visualNovels = results)
                }

            } else {
                val error = "Error: ${response.code()} ${response.message()}"
                Log.e("VisualNovelViewModel", error)
                _uiState.value = VisualNovelState.Error(
                    message = error
                )
            }
        }
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
        val title: String = "title, image.url",
        val filters: List<String> = emptyList()
    ) : VisualNovelEvent
}