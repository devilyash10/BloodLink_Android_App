package com.example.bloodlink.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.bloodlink.presentation.feature_emergency.my_requests.MyRequestsScreen
import com.example.bloodlink.presentation.feature_emergency.success.RequestSentScreen
import com.example.bloodlink.presentation.feature_home.HomeScreen
import com.example.bloodlink.presentation.feature_notifications.NotificationsScreen
import com.example.bloodlink.presentation.feature_profile.history.DonationHistoryScreen
import com.example.bloodlink.presentation.feature_profile.main.ProfileScreen

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
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToSearch = { navController.navigate(Routes.SEARCH_DONORS) },
                onNavigateToEmergency = { navController.navigate(Routes.CREATE_REQUEST) },
                onNavigateToBloodBanks = { navController.navigate(Routes.BLOOD_BANKS) },
                onNavigateToMyRequests = { navController.navigate(Routes.REQUESTS) }
            )
        }
        composable(Routes.REQUESTS) {
            MyRequestsScreen(
                onNavigateBack = { navController.popBackStack() },
                onCreateNewRequest = { navController.navigate(Routes.CREATE_REQUEST) }
            )
        }
        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDonationHistory = { navController.navigate(Routes.DONATION_HISTORY) },
                onNavigateToMyRequests = { navController.navigate(Routes.REQUESTS) },
                onLogoutClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
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
                onDonorClick = { donorId ->
                    navController.navigate(Routes.createDonorProfileRoute(donorId))
                }
            )
        }
        composable(Routes.DONOR_PROFILE) { backStackEntry ->
            // You can extract the donorId here when we implement ViewModels
            val donorId = backStackEntry.arguments?.getString("donorId")
            DonorProfileScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.BLOOD_BANKS) {
            BloodBanksScreen(onNavigateBack = { navController.popBackStack() })
            // Note: Add a button to navigate to MAP_VIEW from BloodBanksScreen if needed
        }
        composable(Routes.MAP_VIEW) {
            MapViewScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.DONATION_HISTORY) {
            DonationHistoryScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}