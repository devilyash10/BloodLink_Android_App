package com.example.bloodlink.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bloodlink.core.navigation.AppNavHost
import com.example.bloodlink.core.navigation.Routes
import com.example.bloodlink.presentation.components.common.BottomNavBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define which screens should show the bottom navigation bar
    val screensWithBottomNav = listOf(
        Routes.HOME,
        Routes.REQUESTS,
        Routes.NOTIFICATIONS,
        Routes.PROFILE
    )

    val showBottomNav = screensWithBottomNav.contains(currentRoute)

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Pop up to the start destination of the graph to avoid building up a large stack
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true // Avoid multiple copies of the same destination
                            restoreState = true // Restore state when reselecting a previously selected item
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        // The AppNavHost handles loading the actual screens
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}