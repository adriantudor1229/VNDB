package com.example.vndbapp.domain.usecase

import com.example.vndbapp.data.mapper.toModel
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.domain.repository.VisualNovelRepository
import com.example.vndbapp.domain.utils.PresentationConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetVisualNovelsByPageUseCase
    @Inject
    constructor(
        private val repository: VisualNovelRepository,
    ) {
        operator fun invoke(page: Int): Flow<Resource<List<VisualNovel>>> {
            return repository.getVisualNovelsByPage(page = page).map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val filtered =
                            resource.data
                                .filter { it.explicit < PresentationConstants.MAX_EXPLICIT_CONTENT_THRESHOLD }
                                .map { it.toModel() }
                        Resource.Success(data = filtered) as Resource<List<VisualNovel>>
                    }
                    is Resource.Error -> resource
                    is Resource.Loading -> resource
                }
            }
        }
    }
