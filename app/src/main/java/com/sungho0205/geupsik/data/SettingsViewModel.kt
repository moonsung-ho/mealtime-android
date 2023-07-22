package com.sungho0205.geupsik.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.model.ElsTimetable
import com.sungho0205.geupsik.model.GradeClass
import com.sungho0205.geupsik.model.MealServiceDiet
import com.sungho0205.geupsik.service.queryMeal
import com.sungho0205.geupsik.service.queryTimetable
import com.sungho0205.geupsik.service.searchGradeClasses
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    val settingFlow: Flow<Settings> = settingsRepository.settingsFlow
    val gradeClasses: SnapshotStateList<GradeClass> = mutableStateListOf()
    val meals: SnapshotStateList<MealServiceDiet> = mutableStateListOf()
    val timetables: SnapshotStateList<ElsTimetable> = mutableStateListOf()
    @RequiresApi(Build.VERSION_CODES.O)
    val selectedDate: MutableState<LocalDate> = mutableStateOf(LocalDate.now())
    val fetchProgress: MutableState<Float> = mutableStateOf(0.0f)

    fun getGradeClasses() {
        viewModelScope.launch {
            settingFlow.collect {
                searchGradeClasses(
                    atptOfcdcScCode = it.atptOfcdcScCode,
                    sdSchulCode = it.sdSchulCode,
                    result = gradeClasses
                )
            }
        }
    }

    fun getMeals(date: String, progress: MutableState<Float>) {
        viewModelScope.launch {
            settingFlow.collect {
                queryMeal(
                    atptOfcdcScCode = it.atptOfcdcScCode,
                    sdSchulCode = it.sdSchulCode,
                    mlsvYmd = date,
                    result = meals,
                    progress = progress
                )
            }
        }
    }

    fun getTimetables(date: String, progress: MutableState<Float>) {
        viewModelScope.launch {
            settingFlow.collect {
                queryTimetable(
                    atptOfcdcScCode = it.atptOfcdcScCode,
                    sdSchulCode = it.sdSchulCode,
                    allTiYmd = date,
                    grade_ = it.grade,
                    class_ = it.class_,
                    result = timetables,
                    progress = progress
                )
            }
        }
    }

    fun updateSchool(
        atptOfcdcScCode: String,
        sdSchulCode: String,
        schulNm: String,
    ) {
        viewModelScope.launch {
            settingsRepository.updateSchool(
                atptOfcdcScCode = atptOfcdcScCode,
                sdSchulCode = sdSchulCode,
                schulNm = schulNm,
            )
        }
    }

    fun updateClassGrade(
        grade_: String, class_: String
    ) {
        viewModelScope.launch {
            settingsRepository.updateGradeClass(
                grade_ = grade_, class_ = class_
            )
        }
    }

    fun updateAlergies(alergyIds: Iterable<com.sungho0205.geupsik.Alergy>) {
        viewModelScope.launch {
            settingsRepository.updateAlergies(alergyIds)
        }
    }
}