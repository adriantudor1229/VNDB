package com.example.vndbapp.presentation.screens.vncharacters

import com.example.vndbapp.data.model.CharacterRole
import com.example.vndbapp.data.model.VNCharacter

fun VNCharacter.roleForVn(vnId: String): String {
    val role = vns.firstOrNull { it.id == vnId }?.role
        ?: vns.firstOrNull()?.role
        ?: role?.let {
            runCatching {
                CharacterRole.valueOf(it.uppercase())
            }.getOrNull()
        }
    return when (role) {
        CharacterRole.MAIN -> "PROTAGONIST"
        CharacterRole.PRIMARY -> "PRIMARY_CAST"
        CharacterRole.SIDE -> "SIDE_CAST"
        CharacterRole.APPEARS -> "APPEARS"
        else -> "UNKNOWN"
    }
}
