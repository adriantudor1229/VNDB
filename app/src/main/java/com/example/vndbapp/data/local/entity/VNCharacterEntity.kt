package com.example.vndbapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "vn_id")
    val vnId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "role")
    val role: String? = null,
    @ColumnInfo(name = "original")
    val original: String? = null,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,
    @ColumnInfo(name = "sexual")
    val sexual: Double = 0.0,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
