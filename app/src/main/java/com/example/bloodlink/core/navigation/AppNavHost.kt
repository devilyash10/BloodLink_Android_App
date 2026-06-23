package com.example.bloodlink.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.presentation.feature_auth.login.LoginScreen
import com.example.bloodlink.presentation.feature_auth.onboarding.OnboardingScreen
import com.example.bloodlink.presentation.feature_auth.signup.SignUpScreen
import com.example.bloodlink.presentation.feature_auth.splash.SplashScreen
import com.example.bloodlink.presentation.feature_bloodbanks.list.BloodBanksScreen
import com.example.bloodlink.presentation.feature_bloodbanks.map.MapViewScreen
import com.example.bloodlink.presentation.feature_donors.list.DonorListScreen
import com.example.bloodlink.presentation.feature_donors.profile.DonorProfileScreen
import com.example.bloodlink.presentation.feature_donors.search.SearchDonorsScreen
import com.example.bloodlink.presentation.feature_emergency.create_request.EmergencyRequestScreen
import com.example.bloodlink.presentation.feature_emergency.my_request.MyRequestScreen
import com.example.bloodlink.presentation.feature_emergency.success.RequestSentScreen
import com.example.bloodlink.presentation.feature_home.HomeScreen
import com.example.bloodlink.presentation.feature_notifications.NotificationsScreen
import com.example.bloodlink.presentation.feature_profile.ProfileRouterScreen
import com.example.bloodlink.presentation.feature_profile.about.AboutUsScreen
import com.example.bloodlink.presentation.feature_profile.edit.EditProfileScreen
import com.example.bloodlink.presentation.feature_profile.history.DonationHistoryScreen
import com.example.bloodlink.presentation.feature_profile.main.ProfileScreen
import com.example.bloodlink.presentation.feature_settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "splash_screen",
        modifier = modifier
    ) {
        // --- SPLASH & ONBOARDING ---
        composable("splash_screen") {
            SplashScreen(
                onNavigateToHome = { navController.navigate("home") { popUpTo("splash_screen") { inclusive = true } } },
                onNavigateToOnboarding = { navController.navigate("onboarding") { popUpTo("splash_screen") { inclusive = true } } }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                onNavigateNext = { navController.navigate("login") { popUpTo("onboarding") { inclusive = true } } }
            )
        }

        // --- AUTHENTICATION ---
        composable("login") {
            LoginScreen(
                onNavigateToHome = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            SignUpScreen(
                onNavigateToHome = { navController.navigate("home") { popUpTo("signup") { inclusive = true } } },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // --- MAIN TABS (Bottom Bar Routes) ---
        composable("home") {
            HomeScreen(
                onNavigateToSearch = { navController.navigate("search_donors") },
                onNavigateToEmergency = { navController.navigate("emergency_request") },
                onNavigateToBloodBanks = { navController.navigate("blood_banks") },
                onNavigateToMyRequests = { navController.navigate("my_requests") },
                onNavigateToRequestDetail = { requestId -> navController.navigate("request_detail/$requestId") }
            )
        }

//        composable("donor_list") {
//            DonorListScreen(
//                onNavigateBack = { navController.popBackStack() },
//                onDonorClick = { donorId -> navController.navigate("donor_profile/$donorId") }
//            )
//        }

        composable("blood_banks") {
            BloodBanksScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMap = { navController.navigate("blood_banks_map") }
            )
        }

        composable("my_requests") {
            MyRequestScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRequestDetail = { requestId -> navController.navigate("request_detail/$requestId") },
                onCreateNewRequest = { navController.navigate("emergency_request") }
            )
        }
        composable("profile"){
            ProfileRouterScreen(
                navController = navController
            )
        }

        composable("donor_profile") {
            ProfileScreen(
                onNavigateToEditProfile = { navController.navigate("edit_profile") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToAboutUs = { navController.navigate("about_us") },
                onLogOut = {
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            )
        }
        // 3. The New Hospital Profile
        composable("hospital_profile") {
            // Grab the ViewModel we just created
            val viewModel: com.example.bloodlink.presentation.feature_profile.hospital.HospitalProfileViewModel = hiltViewModel()
            val hospital by viewModel.hospitalData.collectAsState()
            val logoutEvent by viewModel.logoutEvent.collectAsState()

            LaunchedEffect(logoutEvent) {
                if (logoutEvent) navController.navigate("login") { popUpTo(0) }
            }

            if (hospital != null) {
                com.example.bloodlink.presentation.feature_profile.hospital.HospitalProfileScreen(
                    hospitalName = hospital!!.name,
                    licenseNumber = "Verified: ${hospital!!.id.take(8).uppercase()}",
                    onNavigateToInventory = { navController.navigate("inventory_dashboard") },
                    onNavigateToSettings = { navController.navigate("settings") },
                    onLogOut = { viewModel.logout() }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    androidx.compose.material3.CircularProgressIndicator(color = androidx.compose.ui.graphics.Color(0xFF1976D2))
                }
            }
        }

        // 4. The Inventory Management Dashboard
        composable("inventory_dashboard") {
            com.example.bloodlink.presentation.feature_inventory.InventoryDashboardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- SECONDARY SCREENS ---
        composable("emergency_request") {
            EmergencyRequestScreen(
                onNavigateBack = { navController.popBackStack() },
                onSendRequest = { navController.navigate("request_sent") }
            )
        }

        composable("request_sent") {
            RequestSentScreen(
                requestId = "#REQ98765", // We can make this dynamic later
                onViewRequestsClick = { navController.navigate("my_requests") { popUpTo("home") } },
                onBackToHomeClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }
            )
        }

        composable("request_detail/{requestId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("requestId") ?: ""
            com.example.bloodlink.presentation.feature_requests.detail.RequestDetailScreen(
                requestId = id,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("donor_profile/{donorId}") { backStackEntry ->
            val donorId = backStackEntry.arguments?.getString("donorId") ?: ""
            DonorProfileScreen(
                donorId = donorId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("blood_banks_map") {
            MapViewScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToList = { navController.popBackStack() }
            )
        }

        // 1. The Search Input Screen
        composable("search_donors") {
            SearchDonorsScreen(
                onNavigateBack = { navController.popBackStack() },
                onSearchClicked = { bloodGroup, query, distance ->

                    // THE FIX: Safely encode the strings so "+" doesn't turn into a space!
                    val encodedBlood = Uri.encode(bloodGroup)
                    val safeQuery = query.ifBlank { "ALL" }
                    val encodedQuery = Uri.encode(safeQuery)

                    navController.navigate("donor_list?bloodGroup=$encodedBlood&query=$encodedQuery&distance=$distance")
                }
            )
        }

        // 2. The Donor List (Results) Screen
        composable(
            route = "donor_list?bloodGroup={bloodGroup}&query={query}&distance={distance}",
            arguments = listOf(
                navArgument("bloodGroup") { defaultValue = "All" },
                navArgument("query") { defaultValue = "ALL" },
                navArgument("distance") {
                    type = NavType.FloatType
                    defaultValue = 5f
                }
            )
        ) {
            DonorListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { donorId ->
                    // Future step: Navigate to DonorProfileScreen
                    navController.navigate("donor_profile/$donorId")
                }
            )
        }

        composable("notifications") {
            NotificationsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable("donation_history") {
            DonationHistoryScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable("settings") {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable("edit_profile") {
            EditProfileScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable("about_us") {
            AboutUsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}