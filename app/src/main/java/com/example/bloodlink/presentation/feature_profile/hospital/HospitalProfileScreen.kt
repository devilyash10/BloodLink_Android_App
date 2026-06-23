package com.example.bloodlink.presentation.feature_profile.hospital

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Note: You can create a simple HospitalProfileViewModel that just calls repository.getCurrentHospitalProfile()
// and holds the state, exactly like your Donor ProfileViewModel.

@Composable
fun HospitalProfileScreen(
    hospitalName: String,
    licenseNumber: String = "Verified Institution",
    onNavigateToInventory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Secure Log Out") },
            text = { Text("Are you sure you want to log out of the hospital portal?") },
            containerColor = Color.White,
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogOut() }) {
                    Text("Log Out", color = Color(0xFFE62129), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel", color = Color.Gray) }
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFFFAFAFA)).verticalScroll(rememberScrollState())
    ) {
        // --- ENTERPRISE HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2), RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .padding(top = 64.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier.size(80.dp).background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.LocalHospital, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = hospitalName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)

                Spacer(modifier = Modifier.height(8.dp))
                // Verification Badge
                Row(
                    modifier = Modifier.background(Color(0xFF0D47A1), RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = licenseNumber, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- PRIMARY ACTION: INVENTORY ---
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "Vault Operations", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable { onNavigateToInventory() },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).background(Color(0xFFFFEBEE), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Bloodtype, contentDescription = null, tint = Color(0xFFE62129))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Manage Live Inventory", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Update blood stocks & availability", color = Color.Gray, fontSize = 13.sp)
                    }
                    Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Administration", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))

            HospitalMenuItem(icon = Icons.Default.Settings, title = "Account Settings", onClick = onNavigateToSettings)
            HospitalMenuItem(icon = Icons.Default.SupportAgent, title = "Contact BloodLink Support", onClick = { })

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFE62129))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Secure Log Out", color = Color(0xFFE62129), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun HospitalMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { onClick() }.padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
    }
}