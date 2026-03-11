package com.example.vndbapp.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.vndbapp.data.model.VisualNovel
import com.example.vndbapp.feature_login.DollarsLoginScreen
import com.example.vndbapp.presentation.screens.bottomnav.SettingsScreen
import com.example.vndbapp.presentation.screens.VisualNovelScreen
import com.example.vndbapp.presentation.screens.bottomnav.DollarsChatScreen
import com.example.vndbapp.presentation.screens.vndetails.VisualNovelDetailScreen
import kotlinx.serialization.Serializable

@Serializable data object Login           : NavKey
@Serializable data object VisualNovelList : NavKey
@Serializable data object Chat            : NavKey
@Serializable data object UserSettings    : NavKey
@Serializable data class  VisualNovelDetail(val visualNovel: VisualNovel) : NavKey

private data class BottomTab(val key: NavKey, val icon: ImageVector)

private val bottomTabs = listOf(
    BottomTab(VisualNovelList, Icons.Default.MenuBook),
    BottomTab(Chat,            Icons.Default.Forum),
    BottomTab(UserSettings,    Icons.Default.AccountCircle),
)

@Composable
private fun BottomNavBar(currentRoot: NavKey, onTabSelect: (NavKey) -> Unit) {
    NavigationBar(
        containerColor = com.example.vndbapp.ui.theme.BgApp,
    ) {
        bottomTabs.forEach { tab ->
            val selected = currentRoot == tab.key
            NavigationBarItem(
                selected = selected,
                onClick  = { onTabSelect(tab.key) },
                icon     = {
                    Icon(
                        tab.icon,
                        contentDescription = "",
                        tint = if (selected) com.example.vndbapp.ui.theme.Primary
                               else com.example.vndbapp.ui.theme.TextMuted,
                    )
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    selectedIconColor = com.example.vndbapp.ui.theme.Primary,
                    unselectedIconColor = com.example.vndbapp.ui.theme.TextMuted,
                ),
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavScreen() {
    // Start on Login screen
    val backStack = rememberNavBackStack(Login)

    val currentRoot by remember {
        derivedStateOf {
            backStack.lastOrNull { it is VisualNovelList || it is Chat || it is UserSettings }
                ?: VisualNovelList
        }
    }

    fun switchTab(selected: NavKey) {
        if (selected == currentRoot) return
        while (backStack.lastOrNull() !is VisualNovelList &&
            backStack.lastOrNull() !is Chat &&
            backStack.lastOrNull() !is UserSettings) {
            backStack.removeLastOrNull()
        }
        if (backStack.lastOrNull() != selected) {
            backStack.removeLastOrNull()
            backStack.add(selected)
        }
    }

    NavDisplay(
        backStack     = backStack,
        onBack        = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Login> {
                DollarsLoginScreen(
                    onNavigate = {
                        backStack.removeLastOrNull()
                        backStack.add(VisualNovelList)
                    },
                    onJoin = {
                        backStack.removeLastOrNull()
                        backStack.add(VisualNovelList)
                    }
                )
            }

            entry<VisualNovelList> {
                Scaffold(
                    containerColor = com.example.vndbapp.ui.theme.BgApp,
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Terminal,
                                        contentDescription = null,
                                        tint = com.example.vndbapp.ui.theme.Primary,
                                        modifier = androidx.compose.ui.Modifier.size(22.dp),
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        text = "DOLLARS_DATABASE",
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        fontSize = 16.sp,
                                        letterSpacing = 0.5.sp,
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { }) {
                                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                                }
                                IconButton(onClick = { }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                                }
                            },
                        )
                    },
                    bottomBar = { BottomNavBar(currentRoot, ::switchTab) }
                ) { paddingValues ->
                    Box(Modifier.padding(paddingValues)) {
                        VisualNovelScreen(
                            onNavigateToDetail = { backStack.add(VisualNovelDetail(it)) }
                        )
                    }
                }
            }

            entry<Chat> {
                Scaffold(
                    containerColor = com.example.vndbapp.ui.theme.BgApp,
                    bottomBar = { BottomNavBar(currentRoot, ::switchTab) }
                ) { paddingValues ->
                    Box(Modifier.padding(paddingValues)) {
                        DollarsChatScreen()
                    }
                }
            }

            entry<UserSettings> {
                Scaffold(
                    containerColor = com.example.vndbapp.ui.theme.BgApp,
                    bottomBar = { BottomNavBar(currentRoot, ::switchTab) }
                ) { paddingValues ->
                    Box(Modifier.padding(paddingValues)) {
                        SettingsScreen()
                    }
                }
            }

            entry<VisualNovelDetail> { details ->
                Scaffold(
                    containerColor = com.example.vndbapp.ui.theme.BgApp,
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Terminal,
                                        contentDescription = null,
                                        tint = com.example.vndbapp.ui.theme.Primary,
                                        modifier = Modifier.size(20.dp),
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        text = "ENTRY_VIEWER",
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        fontSize = 15.sp,
                                        letterSpacing = 1.sp,
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = { backStack.removeLastOrNull() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            },
                        )
                    }
                ) { paddingValues ->
                    Box(Modifier.padding(paddingValues)) {
                        VisualNovelDetailScreen(visualNovel = details.visualNovel)
                    }
                }
            }
        },
    )
}
