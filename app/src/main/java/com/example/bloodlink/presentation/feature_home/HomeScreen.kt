package com.example.bloodlink.presentation.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.UrgencyLevel

@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToEmergency: () -> Unit,
    onNavigateToBloodBanks: () -> Unit,
    onNavigateToMyRequests: () -> Unit,
    onNavigateToRequestDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // 1. Observe the user data
    val user by viewModel.userState.collectAsState()

    // 2. Observe the REAL-TIME Firebase blood requests!
    val bloodRequests by viewModel.bloodRequests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Dynamically calculate stats
    // Dynamically calculate stats (Placeholder until we add totalDonations to the User database model)
    val donations = 0
    val livesSaved = donations * 3
    val points = donations * 50

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // --- Top Red Header Section ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFE62129),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    val firstName = user?.fullName?.substringBefore(" ") ?: "Loading"
                    Text(text = "Hello, $firstName 👋", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Be a hero, donate blood", color = Color.White, fontSize = 14.sp)
                }
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White
                )
            }
        }

        // --- Overlapping Main Action Cards ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-30).dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MainActionCard(
                title = "I Want to\nDonate",
                subtitle = "Help someone in need",
                iconTint = Color(0xFFE62129),
                modifier = Modifier.weight(1f)
            )
            MainActionCard(
                title = "I Need\nBlood",
                subtitle = "Request blood\nin emergency",
                iconTint = Color(0xFFE62129),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToEmergency
            )
        }

        // --- Impact Section ---
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "Your Impact", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ImpactItem(value = "$donations", label = "Donations", valueColor = Color(0xFF4CAF50))
                ImpactItem(value = "$livesSaved", label = "Lives Saved", valueColor = Color(0xFFE62129))
                ImpactItem(value = "$points", label = "Points", valueColor = Color(0xFFE62129))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Quick Actions Section ---
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "Quick Actions", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionItem(iconRes = android.R.drawable.ic_menu_search, label = "Search Donors", onClick = onNavigateToSearch)
                QuickActionItem(iconRes = android.R.drawable.ic_menu_mapmode, label = "Blood Banks", onClick = onNavigateToBloodBanks)
                QuickActionItem(iconRes = android.R.drawable.ic_dialog_alert, label = "Emergency", onClick = onNavigateToEmergency)
                QuickActionItem(iconRes = android.R.drawable.ic_menu_recent_history, label = "My Requests", onClick = onNavigateToMyRequests)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- REAL-TIME EMERGENCY REQUESTS SECTION ---
        Column {
            PaddingValues(horizontal = 24.dp).let {
                Text(
                    text = "Active Requests Near You",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading && bloodRequests.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFE62129))
                }
            } else if (bloodRequests.isEmpty()) {
                Text(
                    text = "No active requests right now. You are all caught up!",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            } else {
                // Horizontal Swiping List for Requests
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    items(bloodRequests) { request ->
                        RequestItemCard(
                            request = request,
                            onClick = { onNavigateToRequestDetail(request.requestId) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp)) // Safe space for bottom nav
    }
}

// --- YOUR EXISTING UI COMPONENTS ---

@Composable
fun MainActionCard(title: String, subtitle: String, iconTint: Color, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(40.dp).background(Color(0xFFFFEBEE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🩸", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun ImpactItem(value: String, label: String, valueColor: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun QuickActionItem(iconRes: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color.White, CircleShape)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(painter = painterResource(id = iconRes), contentDescription = label, tint = Color(0xFF0D47A1))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

// --- NEW COMPOSABLE FOR THE LIVE DATA ---

@Composable
fun RequestItemCard(request: BloodRequest, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(280.dp) // Fixed width so they look great in a scrolling row
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Blood Group Badge
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE62129), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = request.bloodGroup, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                // Urgency Badge
                // Urgency Badge (Now handles LOW and MEDIUM via the else branch)
                val urgencyColor = when(request.urgencyLevel.name) {
                    "CRITICAL" -> Color(0xFFD32F2F) // Deep Red
                    "HIGH" -> Color(0xFFF57C00) // Orange
                    else -> Color(0xFF388E3C) // Green (Handles NORMAL, MEDIUM, LOW)
                }
                Text(
                    text = request.urgencyLevel.name,
                    color = urgencyColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = request.patientName, fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = request.hospitalName,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    maxLines = 1 // Keeps the card height consistent
                )
            }
        }
    }
}