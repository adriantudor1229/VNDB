package com.example.vndbapp.data.mapper

import com.example.vndbapp.data.local.entity.VisualNovelEntity
import com.example.vndbapp.data.model.Image
import com.example.vndbapp.data.model.VisualNovel

fun VisualNovel.toEntity(page: Int): VisualNovelEntity {
    return VisualNovelEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = image.url,
        thumbnailUrl = image.thumbnail,
        page = page
    )
}

fun VisualNovelEntity.toModel(): VisualNovel {
    return VisualNovel(
        id = id,
        title = title,
        description = description,
        image = Image(
            url = imageUrl,
            thumbnail = thumbnailUrl
        )
    )
}
