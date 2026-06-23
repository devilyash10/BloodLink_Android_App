package com.example.bloodlink.presentation.feature_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileRouterScreen(
    navController: NavHostController
    // REMOVED authRepository from here!
) {
    var userType by remember { mutableStateOf("LOADING") }

    LaunchedEffect(Unit) {
        try {
            // Grab the UID directly from Firebase Auth
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("Not logged in")

            // Check if they exist in the institutions database
            val institutionDoc = FirebaseFirestore.getInstance().collection("institutions").document(uid).get().await()

            if (institutionDoc.exists()) {
                userType = "HOSPITAL"
            } else {
                userType = "INDIVIDUAL"
            }
        } catch (e: Exception) {
            userType = "INDIVIDUAL" // Default fallback
        }
    }

    when (userType) {
        "LOADING" -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE62129))
            }
        }
        "HOSPITAL" -> {
            LaunchedEffect(Unit) {
                navController.navigate("hospital_profile") {
                    popUpTo("profile") { inclusive = true }
                }
            }
        }
        "INDIVIDUAL" -> {
            LaunchedEffect(Unit) {
                navController.navigate("donor_profile") {
                    popUpTo("profile") { inclusive = true }
                }
            }
        }
    }
}