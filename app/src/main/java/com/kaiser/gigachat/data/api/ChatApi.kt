package com.kaiser.gigachat.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApi {
    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): ChatResponse

    @GET("v1/models")
    suspend fun getModels(): ModelsResponse
}

data class ChatRequest(
    val messages: List<Message>,
    val model: String = "grok-1", // Changed to grok-1 which is more likely to be available
    val stream: Boolean = false,
    val temperature: Float = 0.7f,
    val max_tokens: Int = 150,
    val top_p: Float = 1.0f,
    val frequency_penalty: Float = 0.0f
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val id: String = "",
    val objectType: String = "", // Changed from "object" to "objectType"
    val created: Long = 0,
    val model: String = "",
    val choices: List<Choice> = emptyList()
)

data class Choice(
    val index: Int = 0,
    val message: Message = Message("", ""),
    val finish_reason: String = ""
)

data class ModelsResponse(
    val objectType: String = "", // Changed from "object" to "objectType"
    val data: List<Model> = emptyList()
)

data class Model(
    val id: String,
    val objectType: String, // Changed from "object" to "objectType"
    val created: Long
)