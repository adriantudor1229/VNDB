package com.example.vndbapp.mapper

import com.example.vndbapp.db.VisualNovelsEntity
import com.example.vndbapp.model.Image
import com.example.vndbapp.model.VisualNovel

fun VisualNovel.toEntity(): VisualNovelsEntity {
    return VisualNovelsEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = image.url,
        thumbnailUrl = image.thumbnail
    )
}

fun VisualNovelsEntity.toModel(): VisualNovel {
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
