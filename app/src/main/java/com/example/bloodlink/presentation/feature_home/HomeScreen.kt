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
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
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

@Composable
fun HomeScreen(
    onNavigateToDonateHub: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCreateRequest: () -> Unit,//for Donors(I Need Blood)
    onNavigateToNetworkAlerts: () -> Unit, //for Hospitals(Network Alerts)
    onNavigateToBloodBanks: () -> Unit,
    onNavigateToMyRequests: () -> Unit,
    onNavigateToRequestDetail: (String) -> Unit,
    onNavigateToInventory: () -> Unit, // Added for Hospital routing
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // 1. Observe the NEW state variables from the updated HomeViewModel
    val userName by viewModel.userName.collectAsState()
    val isHospital by viewModel.isHospital.collectAsState()

    // 2. Observe the REAL-TIME Firebase blood requests!
    val bloodRequests by viewModel.bloodRequests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Dynamically calculate stats for individuals
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
        // --- Top Header Section ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isHospital) Color(0xFF1976D2) else Color(0xFFE62129), // Blue for Hospital, Red for Donor
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
                    // Hospitals show their full name, individuals show their first name
                    val displayName = if (isHospital || userName == "Loading...") userName else userName.substringBefore(" ")

                    Text(text = "Hello, $displayName 👋", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isHospital) "BloodLink Enterprise Portal" else "Be a hero, donate blood",
                        color = Color.White,
                        fontSize = 14.sp
                    )
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
            if (isHospital) {
                // ====== HOSPITAL CARDS ======
                MainActionCard(
                    title = "Manage\nVault",
                    subtitle = "Update live inventory",
                    iconTint = Color(0xFF1976D2),
                    icon = Icons.Default.Bloodtype,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToInventory
                )
                MainActionCard(
                    title = "Network\nAlerts",
                    subtitle = "View active emergencies",
                    iconTint = Color(0xFFF57C00),
                    icon = Icons.Default.Warning,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToNetworkAlerts // Or map to requests
                )
            } else {
                // ====== DONOR CARDS ======
                MainActionCard(
                    title = "I Want to\nDonate",
                    subtitle = "Help someone in need",
                    iconTint = Color(0xFFE62129),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToDonateHub
                )
                MainActionCard(
                    title = "I Need\nBlood",
                    subtitle = "Request blood\nin emergency",
                    iconTint = Color(0xFFE62129),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToCreateRequest
                )
            }
        }

        // --- Impact Section (ONLY for Individual Donors) ---
        if (!isHospital) {
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
        }

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
                QuickActionItem(iconRes = android.R.drawable.ic_dialog_alert, label = "Emergency Request", onClick = onNavigateToCreateRequest)
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
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    items(bloodRequests) { request ->
                        RequestItemCard(
                            request = request,
                            modifier = Modifier.width(280.dp), // Keeps it nice and horizontal for Home!
                            onClick = { onNavigateToRequestDetail(request.requestId) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp)) // Safe space for bottom nav
    }
}

// --- UPDATED UI COMPONENTS ---

@Composable
fun MainActionCard(
    title: String,
    subtitle: String,
    iconTint: Color,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null, // Added to support proper Hospital Icons
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(40.dp).background(if (icon != null) Color(0xFFE3F2FD) else Color(0xFFFFEBEE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint)
                } else {
                    Text("🩸", fontSize = 20.sp)
                }
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
@Composable
fun RequestItemCard(
    request: BloodRequest,
    onClick: () -> Unit,
    modifier: Modifier = Modifier // ADDED: This lets us control the width dynamically!
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // --- NEW: Premium Units Needed Indicator ---
                    Text(
                        text = "Need: ${request.unitsRequired} Unit(s)",
                        color = Color.DarkGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    val urgencyColor = when(request.urgencyLevel.name) {
                        "CRITICAL" -> Color(0xFFD32F2F)
                        "HIGH" -> Color(0xFFF57C00)
                        else -> Color(0xFF388E3C)
                    }
                    Text(
                        text = request.urgencyLevel.name,
                        color = urgencyColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = request.patientName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = request.hospitalName,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
    }
}