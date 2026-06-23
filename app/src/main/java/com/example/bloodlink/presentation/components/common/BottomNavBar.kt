package com.example.bloodlink.presentation.components.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Donors : BottomNavItem("search_donors", "Donors", Icons.Default.Search)
    object BloodBanks : BottomNavItem("blood_banks", "Banks", Icons.Default.LocalHospital)
    object Requests : BottomNavItem("my_requests", "Requests", Icons.Default.List)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Donors,
        BottomNavItem.BloodBanks,
        BottomNavItem.Requests,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        items.forEach { item ->

            // --- THE HIGHLIGHT FIX ---
            // If this is the Profile tab, stay highlighted for ALL profile variants!
            val isSelected = if (item.route == "profile") {
                currentRoute == "profile" || currentRoute == "donor_profile" || currentRoute == "hospital_profile"
            } else {
                currentRoute == item.route
            }

            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = isSelected, // Use our new logic here
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFE62129),
                    selectedTextColor = Color(0xFFE62129),
                    indicatorColor = Color(0xFFFFEBEE),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                ),
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}