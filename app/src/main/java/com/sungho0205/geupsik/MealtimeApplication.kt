package com.sungho0205.geupsik

import android.app.Application

class MealtimeApplication: Application() {
    companion object {
        // FIXME api url 수정하기
        const val API_URL = "https://go.kr/meal"
    }

//    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
//        container = AppContainerImpl(this)
    }
}