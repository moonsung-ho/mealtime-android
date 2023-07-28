package com.sungho0205.geupsik.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.sungho0205.geupsik.R
import com.sungho0205.geupsik.ui.theme.Yellow50
import com.sungho0205.geupsik.ui.theme.Yellow700

object Destinations {
    const val HOME_TAB = "home_tab"
    const val TIMETABLE_TAB = "timetable_tab"
    const val SETTING_TAB = "setting_tab"
    const val SETTING_MAIN = "setting"
    const val SETTING_SCHOOL = "setting/school"
    const val SETTING_GRADE_CLASS = "setting/grade_class"
    const val SETTING_ALERGY = "setting/alergy"
    const val SETTING_NOTICES = "setting/notices"
    const val SETTING_NOTICE = "setting/notices/{noticeId}"
}

class NavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(Destinations.HOME_TAB) {
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
        navController.navigate(Destinations.TIMETABLE_TAB) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSetting: () -> Unit = {
        navController.navigate(Destinations.SETTING_MAIN) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettingSchool: () -> Unit = {
        navController.navigate(Destinations.SETTING_SCHOOL) {
//            popUpTo(navController.graph.findStartDestination().id) {
//                saveState = true
//            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettingGradeClass: () -> Unit = {
        navController.navigate(Destinations.SETTING_GRADE_CLASS) {
//            popUpTo(navController.graph.findStartDestination().id) {
//                saveState = true
//            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettingAlergy: () -> Unit = {
        navController.navigate(Destinations.SETTING_ALERGY) {
//            popUpTo(navController.graph.findStartDestination().id) {
//                saveState = true
//            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettingNotices: () -> Unit = {
        navController.navigate(Destinations.SETTING_NOTICES) {
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettingNoticeDetail: (noticeId: String) -> Unit = { noticeId ->
        navController.navigate(
            Destinations.SETTING_NOTICE.replace(
                oldValue = "{noticeId}", newValue = noticeId
            )
        ) {
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
    NavigationBar(
        containerColor = Color.White, contentColor = Yellow700
    ) {
        NavigationBarItem(
            selected = currentRoute == Destinations.HOME_TAB,
            onClick = { navigateToHome() },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.restaurant_menu_black_24dp),
                    contentDescription = stringResource(id = R.string.bnb_home)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = Color.Gray,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = Color.Gray,
                indicatorColor = Yellow50
            ),
            label = { Text(stringResource(id = R.string.bnb_home), fontSize = 10.sp) },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = currentRoute == Destinations.TIMETABLE_TAB,
            onClick = { navigateToTimetable() },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_month_black_24dp),
                    contentDescription = stringResource(id = R.string.bnb_timetable)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = Color.Gray,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = Color.Gray,
                indicatorColor = Yellow50
            ),
            label = { Text(stringResource(id = R.string.bnb_timetable), fontSize = 10.sp) },
            alwaysShowLabel = true,
        )
        NavigationBarItem(
            selected = currentRoute == Destinations.SETTING_TAB || currentRoute == Destinations.SETTING_MAIN,
            onClick = { navigateToSetting() },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.settings_black_24dp),
                    contentDescription = stringResource(id = R.string.bnb_setting)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = Color.Gray,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = Color.Gray,
                indicatorColor = Yellow50
            ),
            label = { Text(stringResource(id = R.string.bnb_setting), fontSize = 10.sp) },
            alwaysShowLabel = true
        )
    }
}