package com.sungho0205.geupsik.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SettingsModelFactory(private val settingsRepository: SettingsRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}