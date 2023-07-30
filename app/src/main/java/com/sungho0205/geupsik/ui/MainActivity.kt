package com.sungho0205.geupsik.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.sungho0205.geupsik.data.*

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        MobileAds.initialize(this)
        val testDeviceIds = listOf<String>("28A7C566C92F6B5E107D8D402DD899D8", "0965157BAD418DBA392F8573A740BDD9")
        val adsConfiguration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(adsConfiguration)
        val settingsViewModel = ViewModelProvider(
            this,
            SettingsModelFactory(SettingsRepository(settingsDataStore))
        )[SettingsViewModel::class.java]
        setContent {
            MealtimeApp(firebaseAnalytics, settingsViewModel)
        }
    }
}
