package com.sungho0205.geupsik.ui.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sungho0205.geupsik.data.*
import com.sungho0205.geupsik.ui.NavigationActions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingNoticeScreen(
    navigationActions: NavigationActions, settingsViewModel: SettingsViewModel, noticeId: Int,
) {
    val notices = settingsViewModel.notices
    val notice = notices.find { notice -> notice.id == noticeId }

    LaunchedEffect(Unit) {
        settingsViewModel.getNotices()
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("공지사항: ${notice?.title}") }, navigationIcon = {
            IconButton(onClick = { navigationActions.navigateToSettingNotices() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
            }
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (notice != null) {
                val format0 =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                format0.timeZone = TimeZone.getTimeZone("UTC") // ISO 8601 is in UTC
                val date = format0.parse(notice.createdAt)
                val format1 = SimpleDateFormat("yyyy-M-dd", Locale.getDefault())

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(notice.title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Text("(${format1.format(date)})")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(notice.description)
            } else {
                Text("공지사항을 찾을 수 없어요.")
                Button(onClick = { navigationActions.navigateToSetting() }) {
                    Text("돌아가기")
                }
            }
        }
    }
}