package com.example.vndbapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.presentation.screens.vndetails.vntab.TabScreen
import com.example.vndbapp.presentation.screens.VisualNovelScreen
import kotlinx.serialization.Serializable

@Serializable
data object VisualNovelList : NavKey

@Serializable
data class VisualNovelDetail(val visualNovel: VisualNovel) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavScreen() {
    val backStack = rememberNavBackStack(VisualNovelList)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<VisualNovelList> {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Visual Novels") }) }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        VisualNovelScreen(
                            onNavigateToDetail = {
                                backStack.add(VisualNovelDetail(it))
                            }
                        )
                    }
                }
            }
            entry<VisualNovelDetail> { details ->
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Details") },
                            navigationIcon = {
                                IconButton(onClick = { backStack.removeLastOrNull() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        TabScreen(
                            visualNovel = details.visualNovel
                        )
                    }
                }
            }
        }
    )
}
