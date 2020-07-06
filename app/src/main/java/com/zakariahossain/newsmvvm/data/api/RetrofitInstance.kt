package com.zakariahossain.newsmvvm.data.api

import com.zakariahossain.newsmvvm.App
import com.zakariahossain.newsmvvm.BuildConfig
import com.zakariahossain.newsmvvm.util.ConnectivityInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    lateinit var appContext: App

    fun init(context: App) {
        this.appContext = context
    }

    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(ConnectivityInterceptor(appContext))
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api by lazy {
        retrofit.create(NewsApi::class.java)
    }
}