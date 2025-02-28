package com.kaiser.gigachat.presentation.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaiser.gigachat.domain.model.Message
import com.kaiser.gigachat.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.launch

class ChatViewModel(private val sendMessageUseCase: SendMessageUseCase) : ViewModel() {
    var state = mutableStateOf(ChatState())
        private set

    fun onMessageInputChanged(text: String) {
        state.value = state.value.copy(inputText = text)
    }

    fun onSendMessage() {
        val message = state.value.inputText
        if (message.isBlank()) return

        state.value = state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val aiResponse = sendMessageUseCase(message)
                val newMessage = Message(message, aiResponse)
                state.value = state.value.copy(
                    messages = listOf(newMessage) + state.value.messages,
                    inputText = "",
                    isLoading = false
                )
            } catch (e: Exception) {
                state.value = state.value.copy(
                    isLoading = false,
                    inputText = "Ошибка: ${e.message ?: "Неизвестная ошибка"}"
                )
            }
        }
    }

    fun loadModels() {
        viewModelScope.launch {
            try {
                val models = sendMessageUseCase.getAvailableModels()
                state.value = state.value.copy(availableModels = models)
            } catch (e: Exception) {
                state.value = state.value.copy(
                    inputText = "Ошибка загрузки моделей: ${e.message ?: "Неизвестная ошибка"}"
                )
            }
        }
    }
}