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
class VisualNovelDetailsViewModel @Inject constructor(
    private val visualNovelRepository: VisualNovelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VisualNovelDetailsState>(
        VisualNovelDetailsState.Loading
    )
    val uiState: StateFlow<VisualNovelDetailsState> = _uiState.asStateFlow()

    fun onEvent(event: VisualNovelDetailsEvent) {
        when (event) {
            is VisualNovelDetailsEvent.GetVisualNovelDetails -> getVisualNovelDetails(
                fields = event.fields,
                id = event.id,
            )
        }
    }

    private fun getVisualNovelDetails(
        fields: String,
        id: String
    ) {
        viewModelScope.launch {
            Log.d("VNDDetails", "Fetching details for ID: $id")
            val result = visualNovelRepository.getVisualNovelDetails(
                fields = fields,
                filters = listOf("id", "=", id)
            )

            Log.d("VNDDetails", "API response code: ${result.code()}")
            Log.d("VNDDetails", "API isSuccessful: ${result.isSuccessful}")
            Log.d("VNDDetails", "API body: ${result.body()}")
            Log.d("VNDDetails", "API errorBody: ${result.errorBody()?.string()}")

            _uiState.update {
                VisualNovelDetailsState.Success(
                    visualNovels =
                        result.body()
                            ?.results ?: emptyList()
                )
            }


        }

    }
}

sealed class VisualNovelDetailsState {
    object Loading : VisualNovelDetailsState()
    data class Success(val visualNovels: List<VisualNovel>) : VisualNovelDetailsState()
    data class Error(val message: String) : VisualNovelDetailsState()
}

sealed interface VisualNovelDetailsEvent {
    data class GetVisualNovelDetails(
        val fields: String = "title, image.thumbnail, description",
        val id: String
    ) : VisualNovelDetailsEvent
}
