package com.example.vndbapp.presentation.screens.vndetails.vntab

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.presentation.screens.vndetails.DescriptionScreen
import com.example.vndbapp.presentation.screens.vndetails.VisualNovelDetailScreen


@Composable
fun VNDetailsTabsScreen(modifier: Modifier = Modifier, visualNovel: VisualNovel) {
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
enum class AppTab(val title: String, val icon: ImageVector) {
    DEFAULT(title = "Default", Icons.Default.Home),
    DESCRIPTION(title = "Description", Icons.Default.Home)
}
