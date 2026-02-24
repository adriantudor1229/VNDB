package com.example.vndbapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.domain.utils.PresentationConstants
import com.example.vndbapp.presentation.components.ImageCard
import com.example.vndbapp.presentation.viewmodel.VisualNovelViewModel

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
    val pagerState = rememberPagerState(
        pageCount = { PresentationConstants.MAX_PAGES },
        initialPage = PresentationConstants.INITIAL_PAGE
    )
    val resource by viewModel.currentPageVns.collectAsState()

    LaunchedEffect(key1 = pagerState.currentPage) {
        val pageNumber = pagerState.currentPage + 1
        viewModel.loadPage(page = pageNumber)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        when (resource) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Success -> {
                val visualNovels = (resource as Resource.Success).data
                VisualNovelGrid(
                    modifier = modifier,
                    visualNovels = visualNovels,
                    onNavigateToDetail = onNavigateToDetail
                )
            }

            is Resource.Error -> {
                val error = resource as Resource.Error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error loading visual novels",
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = error.message,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun VisualNovelGrid(
    modifier: Modifier = Modifier,
    visualNovels: List<VisualNovel>,
    onNavigateToDetail: (VisualNovel) -> Unit,
) {

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = PresentationConstants.GRID_MIN_COLUMN_SIZE_DP.dp)) {
        items(items = visualNovels) { vn ->
            ImageCard(
                imageUrl = vn.image.url ?: "",
                vnId = vn.id,
                onClick = { onNavigateToDetail(vn) },
                modifier = modifier
            )
        }
    }
}
