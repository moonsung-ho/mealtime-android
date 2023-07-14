package com.sungho0205.geupsik.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://mealtimeapi.sungho-moon.workers.dev/"

class APIClient {
    fun getClient(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}