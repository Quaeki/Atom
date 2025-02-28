package com.kaiser.gigachat.data.api

import android.annotation.SuppressLint
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy

@SuppressLint("StaticFieldLeak")
object RetrofitClient {
    private const val BASE_URL = "https://api.x.ai/"
    private const val API_KEY = "xai-OV2FySf5xsRnpcw42OTFUsyV14rBxHcfHDjgSpB9oycRXj9eRx4pHQo9OYVF7zGbfA4aZKkxTzqhKVCL"

    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context
    }

    val chatApi: ChatApi = createRetrofit().create(ChatApi::class.java)

    private fun createRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("194.87.219.252", 20380)))

            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $API_KEY")
                    .addHeader("Content-Type", "application/json")
                    // Modified Accept header to be more specific
                    .addHeader("Accept", "application/json, text/plain, */*")
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