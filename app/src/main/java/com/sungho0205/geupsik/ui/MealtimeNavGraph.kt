package com.sungho0205.geupsik.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.ui.home.HomeScreen
import com.sungho0205.geupsik.ui.setting.SettingGradeClassScreen
import com.sungho0205.geupsik.ui.setting.SettingSchoolScreen
import com.sungho0205.geupsik.ui.setting.SettingScreen
import com.sungho0205.geupsik.ui.timetable.TimetableScreen

@Composable
fun MealtimeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigationActions: NavigationActions,
    startDestination: String = Destinations.HOME_ROUTE,
    settingsViewModel: SettingsViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            route = Destinations.HOME_ROUTE
        ) {
            HomeScreen(
                navigationActions = navigationActions,
                settingsViewModel = settingsViewModel
            )
        }
        composable(
            route = Destinations.TIMETABLE_ROUTE
        ) {
            TimetableScreen(
                navigationActions = navigationActions,
                settingsViewModel = settingsViewModel
            )
        }
        composable(
            route = Destinations.SETTING_ROUTE
        ) {
            SettingScreen(
                navigationActions = navigationActions,
                settingsViewModel = settingsViewModel
            )
        }
        composable(
            route = Destinations.SETTING_SCHOOL
        ) {
            SettingSchoolScreen(
                navigationActions = navigationActions,
                settingsViewModel = settingsViewModel
            )
        }
        composable(
            route = Destinations.SETTING_GRADE_CLASS
        ) {
            SettingGradeClassScreen(
                navigationActions = navigationActions,
                settingsViewModel = settingsViewModel
            )
        }
    }
}