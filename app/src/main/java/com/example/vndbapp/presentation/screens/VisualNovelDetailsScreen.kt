package com.example.vndbapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vndbapp.data.model.Image
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.presentation.components.ImageCardDetails

enum class AppTab(val title: String, val icon: ImageVector) {
    DEFAULT(title = "Default", Icons.Default.Home),
    DESCRIPTION(title = "Description", Icons.Default.Home)
}

@Composable
fun TabScreen(modifier: Modifier = Modifier, visualNovel: VisualNovel) {
    var selectedTab by remember { mutableStateOf(value = AppTab.DEFAULT) }

    Column {
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            AppTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = { Text(tab.title) },
                    icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) }
                )
            }
        }

        when (selectedTab) {
            AppTab.DEFAULT -> VisualNovelDetailScreen(
                modifier = modifier,
                visualNovel = visualNovel
            )

            AppTab.DESCRIPTION -> DescriptionScreen(visualNovel = visualNovel)
        }
    }
}

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

@Composable
fun DescriptionScreen(visualNovel: VisualNovel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Text(text = visualNovel.description)
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
