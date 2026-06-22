package com.example.bloodlink.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                onNavigateToSearch = { navController.navigate("donor_list") },
                onNavigateToEmergency = { navController.navigate("emergency_request") },
                onNavigateToBloodBanks = { navController.navigate("blood_banks") },
                onNavigateToMyRequests = { navController.navigate("my_requests") },
                onNavigateToRequestDetail = { requestId -> navController.navigate("request_detail/$requestId") }
            )
        }

        composable("donor_list") {
            DonorListScreen(
                onNavigateBack = { navController.popBackStack() },
                onDonorClick = { donorId -> navController.navigate("donor_profile/$donorId") }
            )
        }

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

        composable("profile") {
            ProfileScreen(
                onNavigateToEditProfile = { navController.navigate("edit_profile") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToAboutUs = { navController.navigate("about_us") },
                onLogOut = {
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
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

        composable("search_donors") {
            SearchDonorsScreen(
                onNavigateBack = { navController.popBackStack() },
                onSearchClicked = { navController.navigate("donor_list") }
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