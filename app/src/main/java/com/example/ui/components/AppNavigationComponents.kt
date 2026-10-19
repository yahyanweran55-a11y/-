package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AiPrimary
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "الرجوع للخلف"
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.testTag("top_bar_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "الإعدادات"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun AppBottomNav(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBarItem(
            selected = currentScreen is ScreenDestination.Home,
            onClick = { onNavigate(ScreenDestination.Home) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is ScreenDestination.Home) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "الرئيسية"
                )
            },
            label = { Text("الأدوات", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AiPrimary,
                selectedTextColor = AiPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_home")
        )

        NavigationBarItem(
            selected = currentScreen is ScreenDestination.Chat,
            onClick = { onNavigate(ScreenDestination.Chat) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is ScreenDestination.Chat) Icons.AutoMirrored.Filled.Chat else Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = "المحادثة"
                )
            },
            label = { Text("الشات الذكي", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AiPrimary,
                selectedTextColor = AiPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_chat")
        )

        NavigationBarItem(
            selected = currentScreen is ScreenDestination.Saved,
            onClick = { onNavigate(ScreenDestination.Saved) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is ScreenDestination.Saved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "المحفوظات"
                )
            },
            label = { Text("المحفوظات", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AiPrimary,
                selectedTextColor = AiPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_saved")
        )

        NavigationBarItem(
            selected = currentScreen is ScreenDestination.Settings,
            onClick = { onNavigate(ScreenDestination.Settings) },
            icon = {
                Icon(
                    imageVector = if (currentScreen is ScreenDestination.Settings) Icons.Filled.Settings else Icons.Outlined.Settings,
                    contentDescription = "الإعدادات"
                )
            },
            label = { Text("الإعدادات", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AiPrimary,
                selectedTextColor = AiPrimary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_settings")
        )
    }
}
