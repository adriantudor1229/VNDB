package com.example.vndbapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vndbapp.data.model.Image
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.presentation.components.ImageCardDetails

@Composable
fun VisualNovelDetailScreen(
    modifier: Modifier = Modifier,
    visualNovel: VisualNovel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            ImageCardDetails(
                imageThumbnail = visualNovel.image.thumbnail ?: "",
                modifier = Modifier
            )
            Text(
                text = "Title: ${visualNovel.title}",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
        Text(
            text = visualNovel.description,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VisualNovelDetailScreenPreview() = VisualNovelDetailScreen(
    visualNovel = VisualNovel(
        title = "Clannad",
        image = Image(
            url = "",
            thumbnail = ""
        ),
        id = "1",
        description = "Mock description"
    )
)
