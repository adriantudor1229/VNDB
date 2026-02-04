package com.example.vndbapp.screens

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vndbapp.model.VisualNovel
import com.example.vndbapp.mvvm.VisualNovelEvent
import com.example.vndbapp.mvvm.VisualNovelState
import com.example.vndbapp.mvvm.VisualNovelViewModel
import com.example.vndbapp.reutils.ImageCard

@Composable
fun VisualNovelScreen(
    modifier: Modifier = Modifier,
    viewModel: VisualNovelViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VisualNovelListContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier
    )
}

@Composable
private fun VisualNovelListContent(
    modifier: Modifier = Modifier,
    uiState: VisualNovelState,
    onEvent: (VisualNovelEvent) -> Unit,
    onNavigateToDetail: (String) -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { 10 }, initialPage = 0)
    LaunchedEffect(key1 = pagerState.currentPage) {
        val pageNumber = pagerState.currentPage + 1
        onEvent(
            VisualNovelEvent.GetVisualNovel(
                page = pageNumber,
            )
        )
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        when (uiState) {
            is VisualNovelState.Loading -> {
                Text(text = "Loading")
            }

            is VisualNovelState.Success -> {
                VisualNovelGrid(
                    visualNovels = uiState.visualNovels,
                    onNavigateToDetail = onNavigateToDetail,
                    modifier = modifier
                )
            }

            is VisualNovelState.Error -> {
                Text(text = "Error: ${uiState.message}")
            }
        }
    }
}

@Composable
fun VisualNovelGrid(
    modifier: Modifier = Modifier,
    visualNovels: List<VisualNovel>,
    onNavigateToDetail: (String) -> Unit = {},
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(visualNovels) { vn ->
            ImageCard(
                imageUrl = vn.image.url ?: "",
                vnId = vn.id,
                onClick = { onNavigateToDetail(vn.id) },
                modifier = modifier
            )
        }
    }
}
