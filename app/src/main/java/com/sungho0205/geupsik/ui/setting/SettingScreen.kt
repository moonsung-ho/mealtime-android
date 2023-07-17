package com.sungho0205.geupsik.ui.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.ui.NavigationActions

@Composable
fun SettingScreen(
    navigationActions: NavigationActions,
    settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("학교 이름 : ${data.schulNm} ${data.sdSchulCode}")
            if (data.grade == "" && data.class_ == "") {
                Text("선택 없음")
            } else {
                Text("${data.grade}학년 ${data.class_}반")
            }
            Button(onClick = { navigationActions.navigateToSettingSchool() }) {
                Text(text = "학교 설정")
            }
            Button(onClick = { navigationActions.navigateToSettingGradeClass() }) {
                Text(text = "학년 반 설정")
            }
        }
    }
}