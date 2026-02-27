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
import com.example.vndbapp.presentation.screens.VisualNovelScreen
import com.example.vndbapp.presentation.screens.vndetails.VisualNovelDetailScreen
import kotlinx.serialization.Serializable

//@Serializable
//data object Login : NavKey
@Serializable
data object VisualNovelList : NavKey

@Serializable
data class VisualNovelDetail(val visualNovel: VisualNovel) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavScreen() {
    // Start on the Login screen
    val backStack = rememberNavBackStack(VisualNovelList)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            // ── Login ────────────────────────────────────────────────────────
//            entry<Login> {
//                DollarsLoginScreen(
//                    onNavigate = {
//                        // Replace login with the list so Back doesn't return to login
//                        backStack.removeLastOrNull()
//                        backStack.add(VisualNovelList)
//                    },
//                    onJoin = { /* handle join */ }
//                )
//            }

            // ── Visual Novel List ────────────────────────────────────────────
            entry<VisualNovelList> {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Visual Novels") }) },
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        VisualNovelScreen(
                            onNavigateToDetail = {
                                backStack.add(VisualNovelDetail(it))
                            },
                        )
                    }
                }
            }

            // ── Visual Novel Detail ──────────────────────────────────────────
            entry<VisualNovelDetail> { details ->
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Details") },
                            navigationIcon = {
                                IconButton(onClick = { backStack.removeLastOrNull() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                    )
                                }
                            },
                        )
                    },
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        VisualNovelDetailScreen(
                            visualNovel = details.visualNovel,
                        )
                    }
                }
            }
        },
    )
}