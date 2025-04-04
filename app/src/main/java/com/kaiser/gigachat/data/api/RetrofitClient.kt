package com.kaiser.gigachat.data.api

import ChatApi
import android.annotation.SuppressLint
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("StaticFieldLeak")
object RetrofitClient {
    private const val BASE_URL = "https://hubai.loe.gg/v1/"
    private lateinit var context: Context
    private var apiKey: String = "sk-7i4TMEleBLe4wiXU5WD2-A" // Default key

    fun initialize(context: Context, customApiKey: String? = null) {
        this.context = context
        customApiKey?.let { apiKey = it }
    }

    val chatApi: ChatApi by lazy { createRetrofit().create(ChatApi::class.java) }

    private fun createRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}