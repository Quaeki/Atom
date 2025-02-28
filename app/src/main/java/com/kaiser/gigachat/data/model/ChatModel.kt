package com.kaiser.gigachat.data.model

import com.kaiser.gigachat.data.api.ChatApi
import com.kaiser.gigachat.data.api.ChatRequest
import com.kaiser.gigachat.data.api.Message
import java.io.IOException

class ChatModel(private val api: ChatApi) {
    suspend fun sendMessage(message: String): String {
        val request = ChatRequest(
            messages = listOf(
                Message(role = "system", content = "You are a helpful assistant."),
                Message(role = "user", content = message)
            ),
            model = "grok-1", // Using consistent model name
            temperature = 0.5f,
            max_tokens = 100
        )

        try {
            val response = api.sendMessage(request) // Removed redundant API key parameter
            return response.choices.firstOrNull()?.message?.content ?: "No response received"
        } catch (e: IOException) {
            throw IOException("Connection error: ${e.message}", e)
        } catch (e: Exception) {
            throw Exception("Error: ${e.message}", e)
        }
    }

    suspend fun getAvailableModels(): List<String> {
        try {
            val response = api.getModels() // Removed redundant API key parameter
            return response.data.map { it.id }
        } catch (e: IOException) {
            throw IOException("Error retrieving models: ${e.message}", e)
        } catch (e: Exception) {
            throw Exception("Unknown error: ${e.message}", e)
        }
    }
}