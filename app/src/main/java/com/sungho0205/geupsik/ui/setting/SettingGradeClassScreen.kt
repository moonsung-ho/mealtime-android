package com.sungho0205.geupsik.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.*
import com.sungho0205.geupsik.ui.NavigationActions

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(topBar = {
        TopAppBar(title = { Text("학년 반 설정") }, navigationIcon = {
            IconButton(onClick = { navigationActions.navigateToSetting() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
            }
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "학년과 반을 설정하면 급식 메뉴와 시간표를 알려줄 수 있어요.",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(gradeClasses) {
                    ListItem(headlineText = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            RadioButton(
                                selected = data.grade == it.GRADE && data.class_ == it.CLASS_NM,
                                onClick = {
                                    settingsViewModel.updateClassGrade(it.GRADE, it.CLASS_NM)
                                })
                            Text("${it.GRADE}학년 ${it.CLASS_NM}반")
                        }
                    }, modifier = Modifier.clickable {
                        settingsViewModel.updateClassGrade(it.GRADE, it.CLASS_NM)
                    })
                    Divider()
                }
            }
        }
    }
}