package com.example.bloodlink.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {

        // --- AUTHENTICATION FLOW ---
        composable(Routes.SPLASH) {
            SplashScreen(onNavigateNext = {
                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(Routes.SPLASH) { inclusive = true } // Remove splash from backstack
                }
            })
        }
        composable(Routes.ONBOARDING) {
            OnboardingScreen(onNavigateNext = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            })
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        // This clears the backstack so the user can't press "Back" to return to Login
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- MAIN APP FLOW ---
        composable("home") {
            HomeScreen(
                onNavigateToSearch = { navController.navigate("donor_list") },
                onNavigateToEmergency = { navController.navigate("emergency_request") },

                // FIX: Make sure this navigates to the blood_banks placeholder!
                onNavigateToBloodBanks = { navController.navigate("blood_banks") },

                onNavigateToMyRequests = { navController.navigate("my_requests") }
            )
        }
        composable(Routes.REQUESTS) {
            MyRequestScreen(
                onNavigateBack = { navController.popBackStack() },
                onCreateNewRequest = { navController.navigate(Routes.CREATE_REQUEST) }
            )
        }
        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(onNavigateBack = { navController.popBackStack() })
        }
        // Inside your NavHost block:
        composable("profile") {
            ProfileScreen(
                onNavigateToEditProfile = { navController.navigate("edit_profile") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToAboutUs = { navController.navigate("about_us") },
                onLogOut = {
                    navController.navigate("onboarding") { // Or "login" depending on your start screen
                        popUpTo(0) { inclusive = true } // This clears the backstack so they can't hit "back" into the app
                    }
                }
            )
        }
        // --- SECONDARY SCREENS ---
        composable(Routes.CREATE_REQUEST) {
            EmergencyRequestScreen(
                onNavigateBack = { navController.popBackStack() },
                onSendRequest = { navController.navigate(Routes.REQUEST_SENT) }
            )
        }
        composable(Routes.REQUEST_SENT) {
            RequestSentScreen(
                requestId = "#REQ98765",
                onViewRequestsClick = {
                    navController.navigate(Routes.REQUESTS) {
                        popUpTo(Routes.HOME)
                    }
                },
                onBackToHomeClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.SEARCH_DONORS) {
            SearchDonorsScreen(
                onNavigateBack = { navController.popBackStack() },
                onSearchClicked = { navController.navigate(Routes.DONOR_LIST) }
            )
        }
        composable(Routes.DONOR_LIST) {
            DonorListScreen(
                onNavigateBack = { navController.popBackStack() },
                onDonorClick = { donorId -> navController.navigate("donor_profile/$donorId")}
            )
        }
        composable(Routes.DONOR_PROFILE) { backStackEntry ->
            // You can extract the donorId here when we implement ViewModels
            val donorId = backStackEntry.arguments?.getString("donorId")
            DonorProfileScreen(
                donorId = donorId ?: "",
                onNavigateBack = { navController.popBackStack() }
            )
        }
        // --- BLOOD BANKS SECTION ---
        composable("blood_banks") {
            // Note: Update imports if Android Studio complains!
            com.example.bloodlink.presentation.feature_bloodbanks.list.BloodBanksScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMap = { navController.navigate("blood_banks_map") }
            )
        }

        composable("blood_banks_map") {
            com.example.bloodlink.presentation.feature_bloodbanks.map.MapViewScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToList = { navController.popBackStack() } // Pops the map off to return to list
            )
        }
        composable(Routes.DONATION_HISTORY) {
            DonationHistoryScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("edit_profile") {
            EditProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("about_us") {
            AboutUsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("emergency_request") {
            EmergencyRequestScreen(
                onNavigateBack = { navController.popBackStack() },
                onSendRequest = {
                    // After sending, go to My Requests and clear the emergency screen from the backstack
                    navController.navigate("my_requests") {
                        popUpTo("home")
                    }
                }
            )
        }

        composable("my_requests") {
            MyRequestScreen(
                onNavigateBack = { navController.popBackStack() },
                onCreateNewRequest = { navController.navigate("emergency_request") }
            )
        }

//        composable("blood_banks") {
//            // Placeholder until we build the Map screen
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text("Blood Banks Map Coming Soon", fontSize = 18.sp, color = Color.Gray)
//            }
//        }

        composable("donor_profile/{donorId}") { backStackEntry ->
            // Extract the donorId from the route
            val donorId = backStackEntry.arguments?.getString("donorId") ?: ""
            DonorProfileScreen(
                donorId = donorId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}