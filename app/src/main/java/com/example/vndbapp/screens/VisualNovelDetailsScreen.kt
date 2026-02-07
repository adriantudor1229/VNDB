package com.example.vndbapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.vndbapp.model.Image
import com.example.vndbapp.model.VisualNovel
import com.example.vndbapp.reutils.ImageCardDetails

@Composable
fun VisualNovelDetailScreen(
    modifier: Modifier = Modifier,
    visualNovel: VisualNovel
) {
    Column(modifier = modifier.verticalScroll(state = rememberScrollState()))
    {
        Row {
            ImageCardDetails(
                imageThumbnail = visualNovel.image.thumbnail ?: "",
                modifier = modifier
            )
            Text(text = visualNovel.title)
        }
        Text(visualNovel.description)
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
