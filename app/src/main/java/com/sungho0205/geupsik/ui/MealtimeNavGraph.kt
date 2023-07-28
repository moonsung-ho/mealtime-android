package com.sungho0205.geupsik.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.ui.home.HomeScreen
import com.sungho0205.geupsik.ui.setting.SettingAlergyScreen
import com.sungho0205.geupsik.ui.setting.SettingGradeClassScreen
import com.sungho0205.geupsik.ui.setting.SettingNoticeScreen
import com.sungho0205.geupsik.ui.setting.SettingNoticesScreen
import com.sungho0205.geupsik.ui.setting.SettingSchoolScreen
import com.sungho0205.geupsik.ui.setting.SettingScreen
import com.sungho0205.geupsik.ui.timetable.TimetableScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealtimeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigationActions: NavigationActions,
    startDestination: String = Destinations.HOME_TAB,
    settingsViewModel: SettingsViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            route = Destinations.HOME_TAB
        ) {
            HomeScreen(
                navigationActions = navigationActions, settingsViewModel = settingsViewModel
            )
        }
        composable(
            route = Destinations.TIMETABLE_TAB
        ) {
            TimetableScreen(
                navigationActions = navigationActions, settingsViewModel = settingsViewModel
            )
        }
        navigation(
            route = Destinations.SETTING_TAB, startDestination = Destinations.SETTING_MAIN
        ) {
            composable(
                route = Destinations.SETTING_MAIN
            ) {
                SettingScreen(
                    navigationActions = navigationActions, settingsViewModel = settingsViewModel
                )
            }
            composable(
                route = Destinations.SETTING_SCHOOL
            ) {
                SettingSchoolScreen(
                    navigationActions = navigationActions, settingsViewModel = settingsViewModel
                )
            }
            composable(
                route = Destinations.SETTING_GRADE_CLASS
            ) {
                SettingGradeClassScreen(
                    navigationActions = navigationActions, settingsViewModel = settingsViewModel
                )
            }
            composable(route = Destinations.SETTING_ALERGY) {
                SettingAlergyScreen(
                    navigationActions = navigationActions, settingsViewModel = settingsViewModel
                )
            }
            composable(route = Destinations.SETTING_NOTICES) {
                SettingNoticesScreen(
                    navigationActions = navigationActions, settingsViewModel = settingsViewModel
                )
            }
            composable(route = Destinations.SETTING_NOTICE) { navBackStackEntry ->
                val noticeId = navBackStackEntry.arguments?.getString("noticeId")
                noticeId?.let {
                    SettingNoticeScreen(
                        navigationActions = navigationActions,
                        settingsViewModel = settingsViewModel,
                        noticeId = noticeId
                    )
                }
            }
        }
    }
}