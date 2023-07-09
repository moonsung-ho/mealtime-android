package com.sungho0205.geupsik.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // val container = (application as MealtimeApplication).container
        setContent {
            MealtimeApp()
        }
    }
}
