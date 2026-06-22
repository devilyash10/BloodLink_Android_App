package com.example.bloodlink.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bloodlink.core.navigation.AppNavHost
import com.example.bloodlink.presentation.components.common.BottomNavBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // 1. Observe the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. Define exactly which screens get the bottom bar
    val screensWithBottomBar = listOf(
        "home",
        "donor_list",
        "blood_banks",
        "my_requests",
        "profile"
    )

    // 3. Safe check (currentRoute can be null on app launch)
    val showBottomBar = currentRoute in screensWithBottomBar

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->
        // 4. Pass the padding so your screens don't get hidden behind the bar
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavHost(navController = navController)
        }
    }
}