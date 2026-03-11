package com.example.vndbapp.domain.repository

import com.example.vndbapp.data.local.entity.CharacterEntity
import com.example.vndbapp.data.model.Resource
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharactersForVn(vnId: String): Flow<Resource<List<CharacterEntity>>>
}
