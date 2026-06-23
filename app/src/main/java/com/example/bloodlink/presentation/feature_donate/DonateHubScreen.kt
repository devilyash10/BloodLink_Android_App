package com.example.bloodlink.presentation.feature_donate

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.domain.model.BloodBank
import com.example.bloodlink.presentation.components.common.CustomTopAppBar
import com.example.bloodlink.presentation.feature_home.RequestItemCard

@Composable
fun DonateHubScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRequestDetail: (String) -> Unit,
    viewModel: DonateViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Active Emergencies", "Routine Donation")

    val eligibleRequests by viewModel.eligibleRequests.collectAsState()
    val ineligibleRequests by viewModel.ineligibleRequests.collectAsState()
    val bloodBanks by viewModel.bloodBanks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFA))) {
        CustomTopAppBar(title = "Donate Blood", onBackClick = onNavigateBack)

        // --- TWO-WAY SLIDER (TAB ROW) ---
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = Color(0xFFE62129),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color(0xFFE62129),
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) Color(0xFFE62129) else Color.Gray
                        )
                    }
                )
            }
        }

        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFFE62129))
        }

        // --- TAB CONTENT SWITCHER ---
        when (selectedTabIndex) {
            0 -> {
                // TAB 1: EMERGENCY REQUESTS
                LazyColumn(
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (eligibleRequests.isEmpty() && ineligibleRequests.isEmpty() && !isLoading) {
                        item { Text("No active emergencies at this time.", color = Color.Gray) }
                    }

                    if (eligibleRequests.isNotEmpty()) {
                        item {
                            Text("Perfect Matches (You can help!)", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(eligibleRequests) { req ->
                            RequestItemCard(
                                request = req,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onNavigateToRequestDetail(req.requestId) }
                            )
                        }
                    }

                    if (ineligibleRequests.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Other Requests (Incompatible Type)", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(ineligibleRequests) { req ->
                            Box(modifier = Modifier.fillMaxWidth().alpha(0.6f)) {
                                RequestItemCard(
                                    request = req,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { onNavigateToRequestDetail(req.requestId) }
                                )
                            }
                        }
                    }
                }
            }
            1 -> {
                // TAB 2: ROUTINE DONATION (ULTIMATE UI)
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp), // Space for bottom nav
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. Premium Hero Banner
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE8F5E9))
                                .padding(24.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(48.dp).background(Color(0xFF388E3C).copy(alpha = 0.1f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.EventAvailable, contentDescription = null, tint = Color(0xFF388E3C))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text("Plan Your Donation", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Routine donations keep bank vaults stocked and save lives every day.", color = Color(0xFF2E7D32), fontSize = 13.sp, lineHeight = 18.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 2. Section Header
                    item {
                        Text(
                            text = "Verified Blood Centers",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    // 3. The Modern Blood Bank Cards
                    items(bloodBanks) { bank ->
                        RoutineDonationBankCard(
                            bank = bank,
                            onCallClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${bank.contactPhone}") }
                                context.startActivity(intent)
                            },
                            onBookClick = {
                                Toast.makeText(context, "Slot booking opens next week!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

// --- NEW PREMIUM COMPOSABLE FOR PHASE 2 ---

@Composable
fun RoutineDonationBankCard(
    bank: BloodBank,
    onCallClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Section: Icon, Name, and Address
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.size(50.dp).background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.LocalHospital, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = bank.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = bank.address, color = Color.Gray, fontSize = 13.sp, maxLines = 1)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status Badge (Open/Closed) + Distance
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (bank.isOpen) Color(0xFF4CAF50) else Color.Gray))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (bank.isOpen) "Open 24x7" else "Closed",
                            color = if (bank.isOpen) Color(0xFF4CAF50) else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(text = "  •  5.0 km away", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Section: Actions
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Call Button (Outlined)
                OutlinedButton(
                    onClick = onCallClick,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Call Bank", fontWeight = FontWeight.Bold)
                }

                // Book Slot Button (Primary Solid)
                Button(
                    onClick = onBookClick,
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Book Slot", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}