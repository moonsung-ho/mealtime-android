package com.sungho0205.geupsik.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sungho0205.geupsik.data.SettingsViewModel
import com.sungho0205.geupsik.ui.theme.MealtimeTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealtimeApp(settingsViewModel: SettingsViewModel) {
    MealtimeTheme {
        val context = LocalContext.current
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            NavigationActions(navController)
        }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(bottomBar = {
            if (currentRoute == Destinations.HOME_TAB
                || currentRoute == Destinations.TIMETABLE_TAB
                || currentRoute == Destinations.SETTING_TAB
                || currentRoute == Destinations.SETTING_MAIN
            ) {
                Column() {
                    GAds(context = context)
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
        }
    }
}