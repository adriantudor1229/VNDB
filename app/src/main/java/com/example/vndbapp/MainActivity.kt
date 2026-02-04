package com.example.vndbapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.vndbapp.model.VisualNovel
import com.example.vndbapp.mvvm.VisualNovelEvent
import com.example.vndbapp.mvvm.VisualNovelState
import com.example.vndbapp.mvvm.VisualNovelViewModel
import com.example.vndbapp.ui.theme.VNDBAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VNDBAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), topBar = {
                        TopAppBar(
                            title = { Text("Visual Novels") })
                    }) {
                    VisualNovelScreen()
                }
            }
        }
    }
}

@Composable
fun VisualNovelScreen(
    modifier: Modifier = Modifier,
    viewModel: VisualNovelViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VisualNovelListContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
private fun VisualNovelListContent(
    uiState: VisualNovelState,
    onEvent: (VisualNovelEvent) -> Unit,
    modifier: Modifier = Modifier
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
    visualNovels: List<VisualNovel>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(visualNovels) { vn ->
            ImageCard(
                imageUrl = vn.image.url ?: "",
                modifier = modifier
            )
        }
    }
}

@Composable
fun ImageCard(imageUrl: String, modifier: Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp, pressedElevation = 24.dp),
        modifier = modifier
            .padding(top = 1.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)),
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(imageUrl)
                .crossfade(true).build(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .width(200.dp)
                .height(300.dp)
        )
    }
}