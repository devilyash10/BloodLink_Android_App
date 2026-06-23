package com.example.bloodlink.presentation.feature_profile.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val user by viewModel.currentUser.collectAsState()
    val logoutEvent by viewModel.logoutEvent.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(logoutEvent) {
        if (logoutEvent) {
            onLogOut()
        }
    }

    // --- THE LOGOUT DIALOG ---
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out") },
            text = { Text("Are you sure you want to log out of your account?") },
            containerColor = Color.White,
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; viewModel.logout() }) {
                    Text("Yes, Log Out", color = Color(0xFFE62129), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    // --- MAIN UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
    ) {
        // --- HEADER SECTION (Unchanged, matches your screenshot perfectly) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFE62129),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(top = 48.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(80.dp).background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = user?.bloodGroup ?: "--", color = Color(0xFFE62129), fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user?.fullName ?: "Loading...", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text(text = user?.phoneNumber ?: "", color = Color(0xFFFFEBEE), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- NEW: MEDICAL & STATUS CARD ---
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "Medical & Status", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationCity, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Registered City", color = Color.Gray, fontSize = 12.sp)
                            Text(user?.city ?: "--", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF5F5F5))

                    // Donations
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFFE62129), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Total Donations", color = Color.Gray, fontSize = 12.sp)
                            Text("${user?.totalDonations ?: 0} Times", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF5F5F5))

                    // Availability Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Available to Donate", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("You appear in donor searches", color = Color.Gray, fontSize = 12.sp)
                        }
                        Switch(
                            checked = user?.isAvailableAsDonor ?: false,
                            onCheckedChange = { isChecked -> viewModel.toggleAvailability(isChecked) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF4CAF50),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- MENU OPTIONS (Unchanged from your clean layout) ---
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {

            ProfileMenuItem(icon = Icons.Default.Person, title = "Edit Profile", onClick = onNavigateToEditProfile)
            ProfileMenuItem(icon = Icons.Default.Settings, title = "Settings", onClick = onNavigateToSettings)

            ProfileMenuItem(
                icon = Icons.Default.History,
                title = "Donation History",
                onClick = { Toast.makeText(context, "Donation History coming soon!", Toast.LENGTH_SHORT).show() }
            )
            ProfileMenuItem(
                icon = Icons.Default.Favorite,
                title = "Saved Donors",
                onClick = { Toast.makeText(context, "Saved Donors coming soon!", Toast.LENGTH_SHORT).show() }
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(16.dp))

            ProfileMenuItem(
                icon = Icons.Default.SupportAgent,
                title = "Help & Support",
                onClick = { Toast.makeText(context, "Help & Support coming soon!", Toast.LENGTH_SHORT).show() }
            )
            ProfileMenuItem(icon = Icons.Default.Info, title = "About Us", onClick = onNavigateToAboutUs)

            Spacer(modifier = Modifier.height(32.dp))

            // --- LOGOUT BUTTON ---
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFE62129))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Log Out", color = Color(0xFFE62129), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(16.dp)
        )
    }
}