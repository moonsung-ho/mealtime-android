package com.sungho0205.geupsik.ui.setting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.*
import com.sungho0205.geupsik.ui.NavigationActions

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingGradeClassScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value
    val gradeClasses = settingsViewModel.gradeClasses

    LaunchedEffect(data.sdSchulCode, block = {
        settingsViewModel.getGradeClasses()
    })

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navigationActions.navigateToSetting() }) {
                Text("뒤로가기")
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 10.dp),
            ) {
                items(gradeClasses) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 10.dp,
                        onClick = {
                            settingsViewModel.updateClassGrade(it.GRADE, it.CLASS_NM)
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("${it.GRADE}학년 ${it.CLASS_NM}반")
                            if (data.grade == it.GRADE && data.class_ == it.CLASS_NM) {
                                Text("선택")
                            }
                        }
                    }
                }
            }
        }
    }
}