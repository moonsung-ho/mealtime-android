package com.sungho0205.geupsik.ui.home

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.sungho0205.geupsik.ui.theme.Yellow50
import com.sungho0205.geupsik.ui.theme.Yellow500
import com.sungho0205.geupsik.ui.theme.Yellow700
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
    val dateState = settingsViewModel.selectedDate

    val calendar = Calendar.getInstance()
    val context = LocalContext.current

    LaunchedEffect(key1 = data.sdSchulCode, key2 = dateState.value, block = {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        settingsViewModel.getMeals(
            date = dateState.value.format(formatter),
            progress = settingsViewModel.fetchProgress
        )
    })

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateState.value = LocalDate.of(year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val animatedProgress by animateFloatAsState(
        targetValue = settingsViewModel.fetchProgress.value,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
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
                    Text("학교 설정을 하면 급식 메뉴를 볼 수 있어요.")
                    TextButton(onClick = { navigationActions.navigateToSetting() }) {
                        Text("설정하러 가기")
                    }
                }
            } else if (meals.size == 0) {
                Column(modifier = Modifier.padding(innerPadding)) {
                    Text("식단표를 찾을 수가 없어요.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = innerPadding,
                ) {
                    items(meals) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(Yellow50)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = it.MMEAL_SC_NM,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Yellow700
                                    )
                                    Text("(${it.CAL_INFO})", color = Yellow700)
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
                                            menu, color = if (hasMyAlergy) {
                                                Color.Red
                                            } else {
                                                Color.Black
                                            }
                                        )
                                        if (hasAlergy) {
                                            AssistChip(onClick = {
                                                showAlergies.value = !showAlergies.value
                                            }, label = { Text("알러지") }, leadingIcon = {
                                                Icon(
                                                    Icons.Default.Warning, "알러지 경고"
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
                                                            color = Color.Red,
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
}
