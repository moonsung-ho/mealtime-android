package com.sungho0205.geupsik.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.sungho0205.geupsik.Settings
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.ui.theme.MealtimeTheme
import com.sungho0205.geupsik.utils.compareDateAndTimestamp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealtimeApp(firebaseAnalytics: FirebaseAnalytics, settingsViewModel: SettingsViewModel) {
    MealtimeTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            NavigationActions(navController)
        }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val data: Settings =
            settingsViewModel.settingFlow.collectAsState(initial = Settings.getDefaultInstance()).value
        val notices = settingsViewModel.notices

        LaunchedEffect(Unit) {
            navController.addOnDestinationChangedListener { _, destination, arguments ->
                var params = Bundle()
                val screenName = arguments?.getInt("noticeId")?.let { noticeId ->
                    destination.route?.replace("{noticeId}", noticeId.toString())
                } ?: destination.route
                params.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, destination.route)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
            }
        }

        val lifecycleEvent = rememberLifecycleEvent()
        LaunchedEffect(lifecycleEvent) {
            if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
                settingsViewModel.getNotices()
            }
        }

        var openDialog = remember { mutableStateOf(false) }
        val notice = notices.getOrNull(0)

        LaunchedEffect(key1 = notice, block = {
            if (notice != null) {
                openDialog.value = true
            }
        })

        var scrollState = rememberScrollState()

        Scaffold(bottomBar = {
            if (currentRoute == Destinations.HOME_TAB || currentRoute == Destinations.TIMETABLE_TAB || currentRoute == Destinations.SETTING_TAB || currentRoute == Destinations.SETTING_MAIN) {
                Column() {
                    GAds()
                    BottomNavigation(
                        currentRoute = currentRoute,
                        navigateToHome = navigationActions.navigateToHome,
                        navigateToTimetable = navigationActions.navigateToTimetable,
                        navigateToSetting = navigationActions.navigateToSetting
                    )
                }
            }
        }) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = MaterialTheme.colorScheme.background
            ) {
                MealtimeNavGraph(
                    navController = navController,
                    navigationActions = navigationActions,
                    settingsViewModel = settingsViewModel
                )
            }
            // 공지사항 다이얼로그
            if (openDialog.value && notice != null && try {
                    compareDateAndTimestamp(
                        notice.createdAt, data.lastSeenNotice
                    ) > 0
                } catch (t: Throwable) {
                    true
                } && notice.importance == "high"
            ) {
                AlertDialog(onDismissRequest = { openDialog.value = false }, title = {
                    Text("공지사항: ${notice.title}")
                }, text = {
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        Text(notice.description)
                    }
                }, confirmButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                        settingsViewModel.updateLastSeenNotice(
                            System.currentTimeMillis().toString()
                        )
                    }) {
                        Text("다시 보지 않기")
                    }
                })
            }
        }
    }
}

@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return state
}