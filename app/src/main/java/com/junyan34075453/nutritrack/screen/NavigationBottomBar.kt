// NavigationBottomBar.kt
package com.junyan34075453.nutritrack.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
    object Insights : Screen("insights", Icons.Default.Info, "Insights")
    object NutriCoach : Screen("nutricoach", Icons.Default.Person, "NutriCoach")
    object Settings : Screen("settings", Icons.Default.Settings , "Settings")
    object Clinician : Screen("clinician", Icons.Default.Edit, "Clinician")

    companion object {
        fun navigateTo(navController: NavController, screen: Screen, userId: String) {
            navController.navigate(screen.route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Insights,
    Screen.NutriCoach,
    Screen.Settings
)

@Composable
fun BottomNavigationBar(navController: NavController, userId: String) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    Screen.navigateTo(navController, screen, userId)
                }
            )
        }
    }
}