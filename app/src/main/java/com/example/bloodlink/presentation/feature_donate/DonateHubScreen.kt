package com.example.bloodlink.presentation.feature_donate

import android.widget.Toast
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.presentation.components.common.CustomTopAppBar
import com.example.bloodlink.presentation.feature_home.RequestItemCard
import com.example.bloodlink.presentation.feature_bloodbanks.list.ImprovedBloodBankCard

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
                // TAB 1: EMERGENCY REQUESTS (Sorted by Compatibility)
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
                            // Reusing the beautiful card from your HomeScreen!
                            RequestItemCard(
                                request = req,
                                modifier = Modifier.fillMaxWidth(), // Makes it fill the whole screen beautifully!
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
                            // Wrap the card in an alpha to make it look "disabled/incompatible"
                            Box(modifier = Modifier.fillMaxWidth().alpha(0.6f)) {
                                RequestItemCard(
                                    request = req,
                                    modifier = Modifier.fillMaxWidth(), // Makes it fill the whole screen beautifully!
                                    onClick = { onNavigateToRequestDetail(req.requestId) }
                                )
                            }
                        }
                    } else {
                        items(ineligibleRequests) { req ->
                            Box(modifier = Modifier.fillMaxWidth().alpha(0.6f)) { // Use the standard .alpha() here
                                RequestItemCard(
                                    request = req,
                                    modifier = Modifier.fillMaxWidth(), // Makes it fill the whole screen beautifully!
                                    onClick = { onNavigateToRequestDetail(req.requestId) }
                                )
                            }
                        }
                    }
                }
            }
            1 -> {
                // TAB 2: ROUTINE DONATION (Blood Banks & Appointments)
                LazyColumn(
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text("Book a routine donation at a verified center.", color = Color.DarkGray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(bloodBanks) { bank ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(bank.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(bank.address, color = Color.Gray, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Appointment System coming soon!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129)),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Book Appointment")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Quick extension to handle alpha modifier safely
//fun Modifier.alpha(alpha: Float) = this.then(androidx.compose.ui.draw.alpha(alpha))