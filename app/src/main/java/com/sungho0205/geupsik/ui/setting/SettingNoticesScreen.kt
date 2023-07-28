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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingNoticesScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel
) {
    val data: Settings =
        settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value
    val notices = settingsViewModel.notices

    LaunchedEffect(Unit) {
        settingsViewModel.getNotices()
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("공지사항") }, navigationIcon = {
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(notices) {
                    ListItem(headlineText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            val format0 =
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                            format0.timeZone = TimeZone.getTimeZone("UTC") // ISO 8601 is in UTC
                            val date = format0.parse(it.createdAt)
                            val format1 = SimpleDateFormat("yyyy-M-dd", Locale.getDefault())

                            Text(it.title)
                            Text(format1.format(date))
                        }
                    }, modifier = Modifier.clickable {
                        navigationActions.navigateToSettingNoticeDetail(it.id.toString())
                    })
                    Divider()
                }
            }
        }
    }
}