package com.example.vndbapp.presentation.screens.vndetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vndbapp.data.mapper.stripBBCode
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
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VisualNovelDetailScreenPreview() = VisualNovelDetailScreen(
    visualNovel = VisualNovel(
        title = "Clannad",
        image = Image(
            url = "",
            thumbnail = "",
            explicit = 0.0
        ),
        id = "1",
        description = "Mock description"
    )
)
