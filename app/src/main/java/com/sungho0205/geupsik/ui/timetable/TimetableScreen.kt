package com.sungho0205.geupsik.ui.timetable

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.ui.NavigationActions
import com.sungho0205.geupsik.ui.theme.Yellow500
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

val calendar: Calendar = Calendar.getInstance()

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value
    val timetables = settingsViewModel.timetables.filter {
        it.ITRT_CNTNT != null
    }
    val dateState = settingsViewModel.selectedDate

    val context = LocalContext.current

    LaunchedEffect(key1 = data.sdSchulCode, key2 = dateState.value, block = {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        settingsViewModel.getTimetables(
            date = dateState.value.format(formatter),
            progress = settingsViewModel.fetchProgress
        )
    })

    val datePicker = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            dateState.value = LocalDate.of(year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val animatedProgress by animateFloatAsState(
        targetValue = settingsViewModel.fetchProgress.value,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "fetching_progress_timetable"
    )

    Scaffold() { innerPadding ->
        if (settingsViewModel.fetchProgress.value > 0.0f) {
            LinearProgressIndicator(
                modifier = Modifier
                    .semantics(mergeDescendants = true) {}
                    .fillMaxWidth()
                    .offset(x = 0.dp, y = 0.dp),
                progress = animatedProgress,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                Button(onClick = {
                    dateState.value = dateState.value.minusDays(1)
                }) {
                    Icon(Icons.Filled.KeyboardArrowLeft, "어제")
                }
                OutlinedButton(
                    onClick = { datePicker.show() },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    border = BorderStroke(width = 1.dp, color = Yellow500)
                ) {
                    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 dd일(E)")
                    val selectedDate = dateState.value.format(formatter)

                    Text(selectedDate, fontWeight = FontWeight.Bold)
                }
                Button(onClick = {
                    dateState.value = dateState.value.plusDays(1)
                }) {
                    Icon(Icons.Filled.KeyboardArrowRight, "내일")
                }
            }
            if (data.sdSchulCode.isNullOrBlank()) {
                Column(
                    modifier = Modifier.padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("학교 설정을 하면 시간표를 볼 수 있어요.")
                    TextButton(onClick = { navigationActions.navigateToSetting() }) {
                        Text("설정하러 가기")
                    }
                }
            } else if (data.class_.isNullOrBlank()) {
                Column(
                    modifier = Modifier.padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("학년 반을 설정하면 시간표를 볼 수 있어요.")
                    TextButton(onClick = { navigationActions.navigateToSetting() }) {
                        Text("설정하러 가기")
                    }
                }
            } else if (timetables.isEmpty()) {
                Text("시간표를 찾을 수 없어요.")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 10.dp),
                ) {
                    items(timetables) {
                        ListItem(
                            headlineText = { Text(it.ITRT_CNTNT) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}