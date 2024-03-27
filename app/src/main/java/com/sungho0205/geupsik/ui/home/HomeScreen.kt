package com.sungho0205.geupsik.ui.home

import android.app.DatePickerDialog
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.model.EAlergy
import com.sungho0205.geupsik.ui.NavigationActions
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value
    val meals = settingsViewModel.meals
    val calendar = settingsViewModel.calendar
    val dateState = settingsViewModel.selectedDate

    val context = LocalContext.current

    LaunchedEffect(key1 = data.sdSchulCode, key2 = dateState.value, block = {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        settingsViewModel.getMeals(
            date = dateState.value.format(formatter),
            progress = settingsViewModel.fetchProgressMeal
        )
    })

    val datePicker = DatePickerDialog(
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
        targetValue = settingsViewModel.fetchProgressMeal.value,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "fetching_progress_home"
    )

    Scaffold() { innerPadding ->
        if (settingsViewModel.fetchProgressMeal.value > 0.0f) {
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
                OutlinedButton(onClick = {
                    calendar.add(Calendar.DAY_OF_MONTH, -1)
                    dateState.value = dateState.value.minusDays(1)
                }) {
                    Icon(Icons.Filled.KeyboardArrowLeft, "어제")
                }
                OutlinedButton(
                    onClick = { datePicker.show() },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                ) {
                    val formatter = DateTimeFormatter.ofPattern("yyyy. M. d. EE")
                    val selectedDate = dateState.value.format(formatter)

                    Text(selectedDate, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(onClick = {
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
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
                    Text("학교를 등록하면 급식을 볼 수 있어요.", style = MaterialTheme.typography.headlineSmall)
                    TextButton(onClick = { navigationActions.navigateToSetting() }) {
                        Text("학교 설정하러 가기", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else if (meals.size == 0) {
                Column(modifier = Modifier.padding(innerPadding)) {
                    Text("급식이 등록되지 않았어요.", style = MaterialTheme.typography.headlineSmall)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = innerPadding,
                ) {
                    items(meals) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = it.MMEAL_SC_NM,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    it.CAL_INFO,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            val menus = it.DDISH_NM.split("<br/>")

                            menus.map {
                                val regex =
                                    Regex("[,.\\s]+") // Comma, period, or whitespace as delimiters.
                                val parts = it.split(regex).filter { it.isNotEmpty() }
                                val menu = parts[0]
                                val alergiesOfMenu = parts.drop(1).map {
                                    EAlergy.values().find { eAlergy ->
                                        eAlergy.id == it.replace("(", "").replace(")", "")
                                    }
                                }

                                val hasAlergy = alergiesOfMenu.isNotEmpty()

                                val hasMyAlergy =
                                    alergiesOfMenu.map { alergyOfMenu -> alergyOfMenu?.id }
                                        .intersect(
                                            data.alergiesList.toSet()
                                                .map { alergy -> alergy.id }.toSet()
                                        ).isNotEmpty()

                                val showAlergies = remember { mutableStateOf(false) }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        menu,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (hasMyAlergy) {
                                            MaterialTheme.colorScheme.error
                                        } else {
                                            MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.background)
                                        }
                                    )
                                    if (hasAlergy) {
                                        AssistChip(
                                            colors = AssistChipDefaults.assistChipColors(
                                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                            ),
                                            border = AssistChipDefaults.assistChipBorder(
                                                borderColor = MaterialTheme.colorScheme.errorContainer
                                            ),
                                            onClick = {
                                                showAlergies.value = !showAlergies.value
                                            },
                                            label = {
                                                Text(
                                                    "알레르기",
                                                    style = MaterialTheme.typography.labelMedium
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Warning, "알레르기 경고",
                                                    modifier = Modifier.size(18.dp),
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            })
                                    }
                                }
                                if (hasAlergy && showAlergies.value) {
                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp)
                                            .padding(start = 12.dp)
                                    ) {
                                        alergiesOfMenu.map { alergyOfMenu ->
                                            if (data.alergiesList.toSet().any { myAlergy ->
                                                    myAlergy.id == alergyOfMenu?.id
                                                }) {
                                                alergyOfMenu?.label?.let { it ->
                                                    Text(
                                                        it,
                                                        color = MaterialTheme.colorScheme.error,
                                                        modifier = Modifier
                                                            .padding(end = 8.dp)
                                                            .wrapContentWidth(),
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                            } else {
                                                alergyOfMenu?.label?.let { it ->
                                                    Text(
                                                        it,
                                                        color = Color.Gray,
                                                        modifier = Modifier
                                                            .padding(end = 8.dp)
                                                            .wrapContentWidth()
                                                    )
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
    }
}
