package com.example.vndbapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vndbapp.model.VisualNovel
import com.example.vndbapp.mvvm.VisualNovelDetailsEvent
import com.example.vndbapp.mvvm.VisualNovelDetailsState
import com.example.vndbapp.mvvm.VisualNovelDetailsViewModel
import com.example.vndbapp.reutils.ImageCardDetails

@Composable
fun VisualNovelDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: VisualNovelDetailsViewModel = hiltViewModel(),
    vnId: String
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VisualNovelDetailsContent(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        vnId = vnId
    )
}

@Composable
fun VisualNovelDetailsContent(
    modifier: Modifier = Modifier,
    uiState: VisualNovelDetailsState,
    onEvent: (VisualNovelDetailsEvent) -> Unit,
    vnId: String
) {
    LaunchedEffect(key1 = vnId) {
        onEvent(
            VisualNovelDetailsEvent.GetVisualNovelDetails(
                id = vnId
            )
        )
    }

    when (uiState) {
        is VisualNovelDetailsState.Error -> Text("Error")
        is VisualNovelDetailsState.Loading -> Text("Loading")
        is VisualNovelDetailsState.Success -> {
            VisualNovelDetailsList(
                visualNovels = uiState.visualNovels
            )
        }
    }

}

@Composable
fun VisualNovelDetailsList(
    modifier: Modifier = Modifier,
    visualNovels: List<VisualNovel>,
) {
    LazyColumn {
        items(visualNovels) {
            Column {
                Row {
                    ImageCardDetails(
                        imageThumbnail = it.image.thumbnail ?: "",
                        modifier = modifier
                    )
                    Text(text = it.title)
                }
                Text(it.description)
            }
        }
    }
}
