package com.sungho0205.geupsik.ui.setting

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sungho0205.geupsik.Alergy
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.*
import com.sungho0205.geupsik.model.EAlergy
import com.sungho0205.geupsik.ui.NavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingAlergyScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value

    Scaffold(topBar = {
        TopAppBar(title = { Text("알레르기 설정") }, navigationIcon = {
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
                "알레르기를 설정하면 급식 메뉴 중 해당 성분을 강조해서 표시해요.",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                items(EAlergy.values()) { eAlergy ->
                    ListItem(headlineText = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Checkbox(checked = data.alergiesList.any { alergy ->
                                alergy.id == eAlergy.id
                            }, onCheckedChange = { checked ->
                                if (checked) {
                                    val newAlergies = data.alergiesList.toMutableSet()
                                    newAlergies.add(
                                        Alergy.newBuilder().setId(eAlergy.id).build()
                                    )
                                    settingsViewModel.updateAlergies(newAlergies)
                                } else {
                                    val newAlergies = data.alergiesList.toMutableSet()
                                    newAlergies.remove(
                                        Alergy.newBuilder().setId(eAlergy.id).build()
                                    )
                                    settingsViewModel.updateAlergies(newAlergies)
                                }
                            })
                            Text(eAlergy.label)
                        }
                    })
                    Divider()
                }
            }
        }
    }
}