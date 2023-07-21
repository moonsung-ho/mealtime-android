package com.sungho0205.geupsik.ui.timetable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.ui.NavigationActions
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value
    val timetables = settingsViewModel.timetables

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
            settingsViewModel.getTimetables(date = selectedDate)
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
            if (timetables.size == 0) {
                Text("시간표를 찾을 수 없어요.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 10.dp),
                ) {
                    items(timetables) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(2.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text("${it.ITRT_CNTNT}")
                            }
                        }
                    }
                }
            }
        }
    }
}