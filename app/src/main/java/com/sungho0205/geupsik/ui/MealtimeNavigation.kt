package com.sungho0205.geupsik.ui

import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.sungho0205.geupsik.R

object Destinations {
    const val HOME_ROUTE = "home"
    const val TIMETABLE_ROUTE = "timetable"
    const val SETTING_ROUTE = "setting"
    const val SETTING_SCHOOL = "setting_school"
    const val SETTING_GRADE_CLASS = "setting_grade_class"
}

class NavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(Destinations.HOME_ROUTE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
    val navigateToTimetable: () -> Unit = {
        navController.navigate(Destinations.TIMETABLE_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSetting: () -> Unit = {
        navController.navigate(Destinations.SETTING_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettingSchool: () -> Unit = {
        navController.navigate(Destinations.SETTING_SCHOOL) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettingGradeClass: () -> Unit = {
        navController.navigate(Destinations.SETTING_GRADE_CLASS) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
fun BottomNavigation(
    currentRoute: String?,
    navigateToHome: () -> Unit,
    navigateToTimetable: () -> Unit,
    navigateToSetting: () -> Unit
) {
    androidx.compose.material.BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xFF3414E)
    ) {
        BottomNavigationItem(
            selected = currentRoute == Destinations.HOME_ROUTE,
            onClick = { navigateToHome() },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.restaurant_menu_black_24dp),
                    contentDescription = stringResource(id = R.string.bnb_home)
                )
            },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.Gray,
            label = { Text(stringResource(id = R.string.bnb_home), fontSize = 9.sp) },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = currentRoute == Destinations.TIMETABLE_ROUTE,
            onClick = { navigateToTimetable() },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_month_black_24dp),
                    contentDescription = stringResource(id = R.string.bnb_timetable)
                )
            },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.Gray,
            label = { Text(stringResource(id = R.string.bnb_timetable), fontSize = 9.sp) },
            alwaysShowLabel = true,
        )
        BottomNavigationItem(
            selected = currentRoute == Destinations.SETTING_ROUTE,
            onClick = { navigateToSetting() },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.settings_black_24dp),
                    contentDescription = stringResource(id = R.string.bnb_setting)
                )
            },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.Gray,
            label = { Text(stringResource(id = R.string.bnb_setting), fontSize = 9.sp) },
            alwaysShowLabel = true
        )
    }
}