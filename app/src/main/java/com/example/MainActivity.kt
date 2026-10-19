package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AiToolsRegistry
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppTopBar
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ToolWorkbenchScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AiHubViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Ensure 100% Arabic native Right-to-Left (RTL) layout direction
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    AiAppRoot()
                }
            }
        }
    }
}

@Composable
fun AiAppRoot(viewModel: AiHubViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeTool by viewModel.activeTool.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val toolInputText by viewModel.toolInputText.collectAsStateWithLifecycle()
    val toolOutputResult by viewModel.toolOutputResult.collectAsStateWithLifecycle()
    val isToolGenerating by viewModel.isToolGenerating.collectAsStateWithLifecycle()
    val toolErrorMessage by viewModel.toolErrorMessage.collectAsStateWithLifecycle()

    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val chatInputText by viewModel.chatInputText.collectAsStateWithLifecycle()
    val isChatGenerating by viewModel.isChatGenerating.collectAsStateWithLifecycle()

    val savedItems by viewModel.savedItems.collectAsStateWithLifecycle()
    val savedFilterQuery by viewModel.savedFilterQuery.collectAsStateWithLifecycle()
    val customApiKey by viewModel.customApiKey.collectAsStateWithLifecycle()

    val topBarTitle = when (currentScreen) {
        is ScreenDestination.Home -> "شامل AI"
        is ScreenDestination.ToolWorkbench -> activeTool?.title ?: "الأداة الذكية"
        is ScreenDestination.Chat -> "الشات الذكي"
        is ScreenDestination.Saved -> "المحفوظات والمفضلة"
        is ScreenDestination.Settings -> "الإعدادات"
    }

    val canNavigateBack = currentScreen is ScreenDestination.ToolWorkbench

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                title = topBarTitle,
                canNavigateBack = canNavigateBack,
                onNavigateBack = { viewModel.navigateTo(ScreenDestination.Home) },
                onSettingsClick = { viewModel.navigateTo(ScreenDestination.Settings) }
            )
        },
        bottomBar = {
            AppBottomNav(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is ScreenDestination.Home -> {
                    HomeScreen(
                        selectedCategory = selectedCategory,
                        onSelectCategory = { viewModel.selectCategory(it) },
                        searchQuery = searchQuery,
                        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                        onToolClick = { viewModel.openTool(it) },
                        onOpenChat = { viewModel.navigateTo(ScreenDestination.Chat) }
                    )
                }

                is ScreenDestination.ToolWorkbench -> {
                    val tool = activeTool ?: AiToolsRegistry.getToolById(screen.toolId) ?: AiToolsRegistry.tools.first()
                    ToolWorkbenchScreen(
                        tool = tool,
                        inputText = toolInputText,
                        onInputChanged = { viewModel.onToolInputChanged(it) },
                        outputResult = toolOutputResult,
                        isGenerating = isToolGenerating,
                        errorMessage = toolErrorMessage,
                        onGenerate = { viewModel.generateToolResult() },
                        onUseSamplePrompt = { viewModel.useSamplePrompt(it) },
                        onCopyToClipboard = { viewModel.copyToClipboard(it) },
                        onShareText = { viewModel.shareText(it) },
                        onSaveToFavorites = { viewModel.saveCurrentOutputToFavorites() },
                        onBack = { viewModel.navigateTo(ScreenDestination.Home) }
                    )
                }

                is ScreenDestination.Chat -> {
                    ChatScreen(
                        messages = chatMessages,
                        inputText = chatInputText,
                        onInputChanged = { viewModel.onChatInputChanged(it) },
                        isGenerating = isChatGenerating,
                        onSendMessage = { viewModel.sendChatMessage(it) },
                        onClearChat = { viewModel.clearChat() },
                        onCopyToClipboard = { viewModel.copyToClipboard(it) },
                        onShareText = { viewModel.shareText(it) },
                        onBackToHome = { viewModel.navigateTo(ScreenDestination.Home) }
                    )
                }

                is ScreenDestination.Saved -> {
                    SavedScreen(
                        savedItems = savedItems,
                        filterQuery = savedFilterQuery,
                        onFilterQueryChanged = { viewModel.onSavedFilterQueryChanged(it) },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onDeleteItem = { viewModel.deleteSavedItem(it) },
                        onCopyToClipboard = { viewModel.copyToClipboard(it) },
                        onShareText = { viewModel.shareText(it) },
                        onBackToHome = { viewModel.navigateTo(ScreenDestination.Home) }
                    )
                }

                is ScreenDestination.Settings -> {
                    SettingsScreen(
                        currentApiKey = customApiKey,
                        onSaveApiKey = { viewModel.updateApiKey(it) },
                        onBackToHome = { viewModel.navigateTo(ScreenDestination.Home) }
                    )
                }
            }
        }
    }
}
