package com.sungho0205.geupsik.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sungho0205.geupsik.ui.theme.MealtimeTheme

@Composable
fun MealtimeApp() {
    MealtimeTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            NavigationActions(navController)
        }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(bottomBar = {
            BottomNavigation(
                currentRoute = currentRoute,
                navigateToHome = navigationActions.navigateToHome,
                navigateToTimetable = navigationActions.navigateToTimetable,
                navigateToSetting = navigationActions.navigateToSetting
            )
        }) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = MaterialTheme.colors.background
            ) {
                MealtimeNavGraph(
                    navController = navController
                )
            }
        }
    }
}