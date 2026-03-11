package com.example.vndbapp.data.mapper

import com.example.vndbapp.data.local.entity.CharacterEntity
import com.example.vndbapp.data.model.VNCharacter
import com.example.vndbapp.data.model.VNCharacterImage

fun VNCharacter.toEntity(vnId: String): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        original = original,
        description = description,
        imageUrl = image?.url,
        sexual = image?.sexual ?: 0.0,
        vnId = vnId,
    )
}

fun CharacterEntity.toModel(): VNCharacter {
    return VNCharacter(
        id = id,
        name = name,
        original = original,
        description = description,
        image = imageUrl?.let {
            VNCharacterImage(
                url = it,
                sexual = sexual,
            )
        },
    )
}
