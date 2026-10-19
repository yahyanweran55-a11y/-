package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiApiClient
import com.example.data.local.AppDatabase
import com.example.data.local.SavedAiItem
import com.example.data.model.AiTool
import com.example.data.model.AiToolsRegistry
import com.example.data.model.ChatMessage
import com.example.data.model.ToolCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    data object Home : ScreenDestination()
    data class ToolWorkbench(val toolId: String) : ScreenDestination()
    data object Chat : ScreenDestination()
    data object Saved : ScreenDestination()
    data object Settings : ScreenDestination()
}

class AiHubViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val savedItemDao = db.savedItemDao()
    private val apiClient = GeminiApiClient(application)

    // Navigation & Screen State
    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // Home Screen Filters
    private val _selectedCategory = MutableStateFlow(ToolCategory.ALL)
    val selectedCategory: StateFlow<ToolCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Active Tool State (Workbench)
    private val _activeTool = MutableStateFlow<AiTool?>(null)
    val activeTool: StateFlow<AiTool?> = _activeTool.asStateFlow()

    private val _toolInputText = MutableStateFlow("")
    val toolInputText: StateFlow<String> = _toolInputText.asStateFlow()

    private val _toolOutputResult = MutableStateFlow("")
    val toolOutputResult: StateFlow<String> = _toolOutputResult.asStateFlow()

    private val _isToolGenerating = MutableStateFlow(false)
    val isToolGenerating: StateFlow<Boolean> = _isToolGenerating.asStateFlow()

    private val _toolErrorMessage = MutableStateFlow<String?>(null)
    val toolErrorMessage: StateFlow<String?> = _toolErrorMessage.asStateFlow()

    // Chat Screen State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = ChatMessage.Sender.AI,
                text = "مرحباً بك في شامل AI! 🌟\nأنا مساعدك الذكي الشامل، مجاني بالكامل وبدون أي حاجة لتسجيل دخول.\nكيف يمكنني مساعدتك اليوم؟ يمكنك سؤالي عن أي شيء، أو طلب نصيحة، برمجة، ترجمة، أو كتابة محتوى!"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _chatInputText = MutableStateFlow("")
    val chatInputText: StateFlow<String> = _chatInputText.asStateFlow()

    private val _isChatGenerating = MutableStateFlow(false)
    val isChatGenerating: StateFlow<Boolean> = _isChatGenerating.asStateFlow()

    // Saved Items from Room
    val savedItems: StateFlow<List<SavedAiItem>> = savedItemDao.getAllItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filter Saved Items
    private val _savedFilterQuery = MutableStateFlow("")
    val savedFilterQuery: StateFlow<String> = _savedFilterQuery.asStateFlow()

    // Settings API Key State
    private val _customApiKey = MutableStateFlow(apiClient.getEffectiveApiKey())
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    // Navigation Actions
    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun openTool(tool: AiTool) {
        _activeTool.value = tool
        _toolInputText.value = ""
        _toolOutputResult.value = ""
        _toolErrorMessage.value = null
        _currentScreen.value = ScreenDestination.ToolWorkbench(tool.id)
    }

    fun selectCategory(category: ToolCategory) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onToolInputChanged(text: String) {
        _toolInputText.value = text
    }

    fun onChatInputChanged(text: String) {
        _chatInputText.value = text
    }

    fun onSavedFilterQueryChanged(query: String) {
        _savedFilterQuery.value = query
    }

    fun useSamplePrompt(prompt: String) {
        _toolInputText.value = prompt
    }

    // Execute Tool Generation
    fun generateToolResult() {
        val tool = _activeTool.value ?: return
        val prompt = _toolInputText.value.trim()
        if (prompt.isBlank()) {
            _toolErrorMessage.value = "يرجى كتابة نص أو فكرة أولاً"
            return
        }

        viewModelScope.launch {
            _isToolGenerating.value = true
            _toolErrorMessage.value = null
            _toolOutputResult.value = ""

            val result = apiClient.generateContent(
                prompt = prompt,
                systemInstruction = tool.systemPromptInstructions
            )

            result.onSuccess { output ->
                _toolOutputResult.value = output
                // Auto-save generation to local history
                savedItemDao.insertItem(
                    SavedAiItem(
                        toolId = tool.id,
                        toolTitle = tool.title,
                        inputPrompt = prompt,
                        outputContent = output,
                        isFavorite = false
                    )
                )
            }.onFailure { error ->
                _toolErrorMessage.value = error.message ?: "حدث خطأ غير متوقع، يرجى المحاولة ثانية"
            }

            _isToolGenerating.value = false
        }
    }

    // Execute Chat Message
    fun sendChatMessage(presetText: String? = null) {
        val messageText = (presetText ?: _chatInputText.value).trim()
        if (messageText.isBlank()) return

        val userMessage = ChatMessage(
            sender = ChatMessage.Sender.USER,
            text = messageText
        )
        _chatMessages.value = _chatMessages.value + userMessage
        if (presetText == null) {
            _chatInputText.value = ""
        }

        viewModelScope.launch {
            _isChatGenerating.value = true

            // Build conversational context
            val historyContext = _chatMessages.value.takeLast(6).joinToString("\n") {
                val speaker = if (it.sender == ChatMessage.Sender.USER) "المستخدم" else "المساعد"
                "$speaker: ${it.text}"
            }

            val chatPrompt = """
                سياق المحادثة السابقة:
                $historyContext
                
                رسالة المستخدم الحالية:
                $messageText
            """.trimIndent()

            val result = apiClient.generateContent(
                prompt = chatPrompt,
                systemInstruction = "أنت مساعد ذكاء اصطناعي عربي ودود وشامل ومحترف، يدعم الإجابة المفصلة والدقيقة والمنظمة باللغة العربية مع إبداء الاهتمام والترحيب."
            )

            result.onSuccess { reply ->
                _chatMessages.value = _chatMessages.value + ChatMessage(
                    sender = ChatMessage.Sender.AI,
                    text = reply
                )
            }.onFailure { error ->
                _chatMessages.value = _chatMessages.value + ChatMessage(
                    sender = ChatMessage.Sender.AI,
                    text = "عذراً، حدث خطأ أثناء معالجة الرد: ${error.message}",
                    isError = true
                )
            }

            _isChatGenerating.value = false
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                sender = ChatMessage.Sender.AI,
                text = "تم بدء محادثة جديدة! كيف يمكنني مساعدتك الآن؟ 🚀"
            )
        )
    }

    // Clipboard and Share Utilities
    fun copyToClipboard(text: String, label: String = "نص الذكاء الاصطناعي") {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(getApplication(), "تم النسخ بنجاح! 📋", Toast.LENGTH_SHORT).show()
    }

    fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chooser = Intent.createChooser(intent, "مشاركة النص عبر").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        getApplication<Application>().startActivity(chooser)
    }

    // Local Storage Actions
    fun toggleFavorite(item: SavedAiItem) {
        viewModelScope.launch {
            savedItemDao.updateItem(item.copy(isFavorite = !item.isFavorite))
        }
    }

    fun deleteSavedItem(item: SavedAiItem) {
        viewModelScope.launch {
            savedItemDao.deleteItem(item)
            Toast.makeText(getApplication(), "تم حذف العنصر من السجل", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveCurrentOutputToFavorites() {
        val tool = _activeTool.value ?: return
        val output = _toolOutputResult.value
        val input = _toolInputText.value
        if (output.isBlank()) return

        viewModelScope.launch {
            savedItemDao.insertItem(
                SavedAiItem(
                    toolId = tool.id,
                    toolTitle = tool.title,
                    inputPrompt = input,
                    outputContent = output,
                    isFavorite = true
                )
            )
            Toast.makeText(getApplication(), "تمت الإضافة إلى المفضلة! ⭐", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateApiKey(newKey: String) {
        apiClient.saveCustomApiKey(newKey)
        _customApiKey.value = newKey.trim()
        Toast.makeText(getApplication(), "تم حفظ المفتاح بنجاح! ✅", Toast.LENGTH_SHORT).show()
    }
}
