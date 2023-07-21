package com.sungho0205.geupsik.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.model.EAlergy
import com.sungho0205.geupsik.ui.NavigationActions
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.logging.SimpleFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value
    val meals = settingsViewModel.meals
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val isShowDatePicker = remember { mutableStateOf(false) }

    val selectedDate = datePickerState.selectedDateMillis?.let {
        SimpleDateFormat(
            "yyyyMMdd", Locale.getDefault()
        ).format(Date(it))
    }

    LaunchedEffect(key1 = data.sdSchulCode, key2 = selectedDate, block = {
        if (selectedDate != null) {
            settingsViewModel.getMeals(date = selectedDate)
        }
    })

    val selectedDateInButton = datePickerState.selectedDateMillis?.let {
        SimpleDateFormat(
            "yyyy년 M월 dd일", Locale.getDefault()
        ).format(Date(it))
    }

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(onClick = { isShowDatePicker.value = true }) {
                if (selectedDateInButton != null) {
                    Text(selectedDateInButton)
                }
            }
            if (isShowDatePicker.value) {
                DatePickerDialog(onDismissRequest = { isShowDatePicker.value = false },
                    confirmButton = {
                        TextButton(onClick = { isShowDatePicker.value = false }) {
                            Text("선택")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isShowDatePicker.value = false }) {
                            Text("취소")
                        }
                    }) {

                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        modifier = Modifier.width(400.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (meals.size == 0) {
                Text("식단표를 찾을 수가 없어요.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = innerPadding,
                ) {
                    items(meals) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(2.dp),
                        ) {
                            Column(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text("종류: ${it.MMEAL_SC_NM}")
                                Text("열량: ${it.CAL_INFO}")
                                Spacer(modifier = Modifier.height(4.dp))

                                val menus = it.DDISH_NM.split("<br/>")

                                menus.map {
                                    val regex =
                                        Regex("[,.\\s]+") // Comma, period, or whitespace as delimiters.
                                    val parts = it.split(regex).filter { it.isNotEmpty() }
                                    val menu = parts[0]
                                    val eAlergies = parts.drop(1).map {
                                        EAlergy.values().find { eAlergy -> eAlergy.id == it }
                                    }

                                    val hasAlergy =
                                        eAlergies.map { eAlergy -> eAlergy?.id }.intersect(
                                            data.alergiesList.toSet().map { alergy -> alergy.id }
                                                .toSet()
                                        )
                                            .isNotEmpty()

                                    Text(
                                        menu, color = if (hasAlergy) {
                                            Color.Red
                                        } else {
                                            Color.Black
                                        }
                                    )
                                    Column() {
                                        eAlergies.map {
                                            it?.label?.let { it1 -> Text(it1, color = Color.Gray) }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}