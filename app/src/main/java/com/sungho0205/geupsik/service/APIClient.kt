package com.sungho0205.geupsik.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

enum class API(val value: String) {
    OpenNeis("https://open.neis.go.kr/"),
    GitHubs("https://raw.githubusercontent.com/")
}

class APIClient {
    fun getClient(api: API): Retrofit {
        return Retrofit.Builder().baseUrl(api.value)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}