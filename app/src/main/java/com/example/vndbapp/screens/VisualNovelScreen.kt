package com.example.vndbapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vndbapp.mapper.toModel
import com.example.vndbapp.model.VisualNovel
import com.example.vndbapp.mvvm.VisualNovelEvent
import com.example.vndbapp.mvvm.VisualNovelState
import com.example.vndbapp.mvvm.VisualNovelViewModel
import com.example.vndbapp.reutils.ImageCard

@Composable
fun VisualNovelScreen(
    modifier: Modifier = Modifier,

    onNavigateToDetail: (VisualNovel) -> Unit
) {

    VisualNovelListContent(
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier
    )
}

@Composable
private fun VisualNovelListContent(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (VisualNovel) -> Unit,
    viewModel: VisualNovelViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState(pageCount = { 10 }, initialPage = 0)
    val visualNovels by viewModel.currentPageVns.collectAsState()

    LaunchedEffect(pagerState.currentPage) {
        val pageNumber = pagerState.currentPage + 1
        viewModel.loadPage(
            page = pageNumber,
            fields = "title, image.url, image.thumbnail, description",
            filters = emptyList()
        )
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        if (visualNovels.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            VisualNovelGrid(
                visualNovels = visualNovels.map { it.toModel() },
                onNavigateToDetail = onNavigateToDetail,
                modifier = modifier
            )
        }
    }
}

@Composable
fun VisualNovelGrid(
    modifier: Modifier = Modifier,
    visualNovels: List<VisualNovel>,
    onNavigateToDetail: (VisualNovel) -> Unit,
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(visualNovels) { vn ->
            ImageCard(
                imageUrl = vn.image.url ?: "",
                vnId = vn.id,
                onClick = { onNavigateToDetail(vn) },
                modifier = modifier
            )
        }
    }
}
