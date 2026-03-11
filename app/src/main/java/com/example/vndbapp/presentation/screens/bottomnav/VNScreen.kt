package com.example.vndbapp.presentation.screens.bottomnav

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vndbapp.data.model.Resource
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.domain.utils.PresentationConstants
import com.example.vndbapp.presentation.viewmodel.VisualNovelViewModel
import com.example.vndbapp.ui.components.VNCard
import com.example.vndbapp.ui.theme.BgApp
import com.example.vndbapp.ui.theme.BgCard
import com.example.vndbapp.ui.theme.BorderDefault
import com.example.vndbapp.ui.theme.Primary
import com.example.vndbapp.ui.theme.PrimaryBorder
import com.example.vndbapp.ui.theme.PrimaryDim
import com.example.vndbapp.ui.theme.TextMuted
import com.example.vndbapp.ui.theme.TextPrimary

private val Mono = FontFamily.Monospace

private val filterTabs = listOf("ALL_ENTRIES", "ACTIVE", "ARCHIVED")

@Composable
fun VisualNovelScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (VisualNovel) -> Unit,
) {
    VisualNovelListContent(
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier,
    )
}

@Composable
private fun VisualNovelListContent(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (VisualNovel) -> Unit,
    viewModel: VisualNovelViewModel = hiltViewModel(),
) {
    val pagerState =
        rememberPagerState(
            pageCount = { PresentationConstants.MAX_PAGES },
            initialPage = PresentationConstants.INITIAL_PAGE,
        )
    val resource by viewModel.currentPageVns.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = pagerState.currentPage) {
        val pageNumber = pagerState.currentPage + 1
        viewModel.loadPage(page = pageNumber)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgApp)
    ) {
        Spacer(Modifier.height(12.dp))

        // Search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
        )

        Spacer(Modifier.height(12.dp))

        // Filter chips row
        FilterChips(
            tabs = filterTabs,
            selectedIndex = selectedTab,
            onSelect = { selectedTab = it },
        )

        Spacer(Modifier.height(12.dp))

        // Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            when (resource) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Primary)
                    }
                }

                is Resource.Success -> {
                    val visualNovels = (resource as Resource.Success).data
                    val filtered = if (searchQuery.isBlank()) visualNovels
                    else visualNovels.filter {
                        it.title.contains(searchQuery, ignoreCase = true)
                    }
                    VisualNovelGrid(
                        visualNovels = filtered,
                        onNavigateToDetail = onNavigateToDetail,
                    )
                }

                is Resource.Error -> {
                    val error = resource as Resource.Error
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Error loading visual novels",
                                color = MaterialTheme.colorScheme.error,
                            )
                            Text(
                                text = error.message,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(BgCard)
            .border(1.dp, BorderDefault, RoundedCornerShape(4.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$ ",
                fontFamily = Mono,
                fontSize = 13.sp,
                color = Primary,
                fontWeight = FontWeight.Bold,
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = "execute_search --query '...'",
                        fontFamily = Mono,
                        fontSize = 13.sp,
                        color = TextMuted,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = Mono,
                        fontSize = 13.sp,
                        color = TextPrimary,
                    ),
                    cursorBrush = SolidColor(Primary),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun FilterChips(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tabs.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isSelected) PrimaryDim else BgCard)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) PrimaryBorder else BorderDefault,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .clickable { onSelect(index) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    fontFamily = Mono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    color = if (isSelected) Primary else TextMuted,
                )
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
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    ) {
        items(items = visualNovels) { vn ->
            VNCard(
                imageUrl = vn.image.url ?: "",
                onClick = { onNavigateToDetail(vn) },
                modifier = modifier,
                title = vn.title
            )
        }
    }
}
