package com.sungho0205.geupsik.ui.setting

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value

    Scaffold(topBar = {
        TopAppBar(title = { Text("설정") })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(Yellow50)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                ) {
                    Text("현재 설정", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Yellow700)
                    Text(
                        "학교 : ${
                            if (data.schulNm.isNullOrBlank()) {
                                ""
                            } else {
                                data.schulNm
                            }
                        }", color = Yellow500
                    )
                    Text(
                        "학년 반 : ${
                            if (data.class_.isNullOrBlank()) {
                                ""
                            } else {
                                "${data.grade}학년 ${data.class_}반"
                            }
                        }", color = Yellow500
                    )
                    Text("알러지 : ${
                        data.alergiesList.map { myAlergy ->
                            EAlergy.values().find { eAlergy ->
                                eAlergy.id == myAlergy.id
                            }
                        }.map { eAlergy -> eAlergy?.label }.joinToString(", ")
                    }", color = Yellow500
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            ListItem(headlineText = {
                Text(text = "학교 설정")
            }, trailingContent = {
                Icon(Icons.Filled.KeyboardArrowRight, "학교 설정하기")
            }, modifier = Modifier.clickable {
                navigationActions.navigateToSettingSchool()
            })
            ListItem(headlineText = {
                Text(text = "학년 반 설정")
            }, trailingContent = {
                Icon(Icons.Filled.KeyboardArrowRight, "학년 반 설정하기")
            }, modifier = Modifier.clickable {
                navigationActions.navigateToSettingGradeClass()
            })
            ListItem(headlineText = {
                Text(text = "알러지 설정")
            }, trailingContent = {
                Icon(Icons.Filled.KeyboardArrowRight, "알러지 설정하기")
            }, modifier = Modifier.clickable {
                navigationActions.navigateToSettingAlergy()
            })
        }
    }
}