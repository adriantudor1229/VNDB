package com.example.vndbapp.domain.usecase

import com.example.vndbapp.data.mapper.toModel
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VNCharacter
import com.example.vndbapp.domain.repository.CharacterRepository
import com.example.vndbapp.domain.utils.PresentationConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCharactersForVnUseCase
    @Inject
    constructor(
        private val repository: CharacterRepository,
    ) {
        operator fun invoke(vnId: String): Flow<Resource<List<VNCharacter>>> {
            return repository.getCharactersForVn(vnId = vnId).map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val filtered =
                            resource.data
                                .filter { it.sexual < PresentationConstants.MAX_EXPLICIT_CONTENT_THRESHOLD }
                                .map { it.toModel() }
                        Resource.Success(data = filtered) as Resource<List<VNCharacter>>
                    }
                    is Resource.Error -> resource
                    is Resource.Loading -> resource
                }
            }
        }
    }
