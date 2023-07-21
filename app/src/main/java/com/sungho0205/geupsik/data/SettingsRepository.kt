package com.sungho0205.geupsik.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.model.Alergy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings = Settings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Settings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read settings.proto", exception)
        }
    }

    override suspend fun writeTo(
        t: Settings, output: OutputStream
    ) = t.writeTo(output)
}

private const val SETTINGS_NAME = "settings"
private const val DATA_STORE_FILE_NAME = "settings.pb"

val Context.settingsDataStore: DataStore<Settings> by dataStore(
    fileName = DATA_STORE_FILE_NAME, serializer = SettingsSerializer
)

class SettingsRepository(
    private val settingsDataStore: DataStore<Settings>
) {
    val settingsFlow: Flow<Settings> = settingsDataStore.data

    suspend fun updateSchool(
        atptOfcdcScCode: String,
        sdSchulCode: String,
        schulNm: String,
    ) {
        settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().setAtptOfcdcScCode(atptOfcdcScCode)
                .setSdSchulCode(sdSchulCode).setSchulNm(schulNm).setGrade("").setClass_("").build()
        }
    }

    suspend fun updateGradeClass(
        grade_: String,
        class_: String,
    ) {
        settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().setGrade(grade_).setClass_(class_).build()
        }
    }

    suspend fun updateAlergies(
        alergyIds: Iterable<com.sungho0205.geupsik.Alergy>
    ) {
        settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder().clearAlergies().addAllAlergies(alergyIds).build()
        }
    }
}