package com.example.vndbapp.data.mapper

import com.example.vndbapp.data.local.entity.CharacterEntity
import com.example.vndbapp.data.model.CharacterRole
import com.example.vndbapp.data.model.CharacterVn
import com.example.vndbapp.data.model.VNCharacter
import com.example.vndbapp.data.model.VNCharacterImage

fun VNCharacter.toEntity(vnId: String,role: String? = null): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        original = original,
        description = description,
        imageUrl = image?.url,
        sexual = image?.sexual ?: 0.0,
        vnId = vnId,
        role = role
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
        vns = listOf(
            CharacterVn(
                id = vnId,
                spoiler = 0,
                role = role?.let { CharacterRole.valueOf(it.uppercase()) }
                    ?: CharacterRole.SIDE,
                release = null
            )
        ),
        role = role
    )
}
