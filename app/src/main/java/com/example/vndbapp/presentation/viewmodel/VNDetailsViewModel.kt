package com.example.vndbapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VNCharacter
import com.example.vndbapp.domain.usecase.GetCharactersForVnUseCase
import com.example.vndbapp.domain.utils.PresentationConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VisualNovelDetailViewModel
    @Inject
    constructor(
        private val getCharactersForVnUseCase: GetCharactersForVnUseCase,
    ) : ViewModel() {

        private val _vnId = MutableStateFlow("")

        val characters: StateFlow<Resource<List<VNCharacter>>> =
            _vnId.flatMapLatest { vnId ->
                if (vnId.isBlank()) flowOf(Resource.Loading)
                else getCharactersForVnUseCase(vnId = vnId)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = PresentationConstants.STATE_RETENTION_TIMEOUT_MS),
                initialValue = Resource.Loading,
            )

        fun loadCharacters(vnId: String) {
            _vnId.value = vnId
        }
    }
