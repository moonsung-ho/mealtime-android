package com.sungho0205.geupsik.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.sungho0205.geupsik.data.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsViewModel = ViewModelProvider(
            this,
            SettingsModelFactory(SettingsRepository(settingsDataStore))
        )[SettingsViewModel::class.java]
        setContent {
            MealtimeApp(settingsViewModel)
        }
    }
}
