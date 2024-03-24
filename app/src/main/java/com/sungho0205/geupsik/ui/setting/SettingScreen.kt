package com.sungho0205.geupsik.ui.setting

import androidx.compose.foundation.BorderStroke
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value

    Scaffold(topBar = {
        TopAppBar(title = { Text("더보기") })
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
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                ) {
                    Text("현재 설정", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        "학교 : ${
                            if (data.schulNm.isNullOrBlank()) {
                                ""
                            } else {
                                data.schulNm
                            }
                        }"
                    )
                    Text(
                        "학년/반 : ${
                            if (data.class_.isNullOrBlank()) {
                                ""
                            } else {
                                "${data.grade}학년 ${data.class_}반"
                            }
                        }"
                    )
                    Text("알레르기 : ${
                        data.alergiesList.map { myAlergy ->
                            EAlergy.values().find { eAlergy ->
                                eAlergy.id == myAlergy.id
                            }
                        }.map { eAlergy -> eAlergy?.label }.joinToString(", ")
                    }"
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            ListItem(headlineText = {
                Text(text = "학교 설정하기")
            }, trailingContent = {
                Icon(Icons.Filled.KeyboardArrowRight, "학교 설정하기")
            }, modifier = Modifier.clickable {
                navigationActions.navigateToSettingSchool()
            })
            ListItem(headlineText = {
                Text(text = "학년/반 설정하기")
            }, trailingContent = {
                Icon(Icons.Filled.KeyboardArrowRight, "학년 반 설정하기")
            }, modifier = Modifier.clickable {
                navigationActions.navigateToSettingGradeClass()
            })
            ListItem(headlineText = {
                Text(text = "알레르기 설정하기")
            }, trailingContent = {
                Icon(Icons.Filled.KeyboardArrowRight, "알레르기 설정하기")
            }, modifier = Modifier.clickable {
                navigationActions.navigateToSettingAlergy()
            })
            ListItem(headlineText = {
                Text(text = "공지사항")
            }, trailingContent = {
                Icon(Icons.Filled.KeyboardArrowRight, "공지사항 보기")
            }, modifier = Modifier.clickable {
                navigationActions.navigateToSettingNotices()
            })
        }
    }
}