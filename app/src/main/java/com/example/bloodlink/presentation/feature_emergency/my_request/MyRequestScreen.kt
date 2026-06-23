package com.example.bloodlink.presentation.feature_emergency.my_request

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.domain.model.BloodRequest
import com.example.bloodlink.domain.model.RequestStatus
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun MyRequestScreen(
    onNavigateBack: () -> Unit,
    onCreateNewRequest: () -> Unit,
    onNavigateToRequestDetail: (String) -> Unit,
    viewModel: MyRequestsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val requests by viewModel.filteredRequests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("All", "Active", "Completed")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewRequest,
                containerColor = Color(0xFFE62129),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "New Request")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA))
        ) {
            CustomTopAppBar(
                title = "My Requests",
                onBackClick = onNavigateBack
            )

            // --- Premium Tab Row ---
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Color(0xFFE62129),
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color(0xFFE62129)
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            // Trigger the reactive ViewModel filter
                            val filter = when(title) {
                                "Active" -> RequestFilter.ACTIVE
                                "Completed" -> RequestFilter.COMPLETED
                                else -> RequestFilter.ALL
                            }
                            viewModel.setFilter(filter)
                        },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) Color(0xFFE62129) else Color.Gray
                            )
                        }
                    )
                }
            }

            // --- Content State Handling ---
            if (isLoading && requests.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFE62129))
                }
            } else if (requests.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "No requests found for this category.", color = Color.Gray, fontSize = 16.sp)
                    }
                }
            } else {
                // --- Request List ---
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(requests, key = { it.requestId }) { request -> // Added key for better compose performance
                        MyRequestPremiumCard(
                            request = request,
                            onClick = { onNavigateToRequestDetail(request.requestId) },
                            onMarkCompleted = { viewModel.markAsCompleted(request.requestId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyRequestPremiumCard(
    request: BloodRequest,
    onClick: () -> Unit,
    onMarkCompleted: () -> Unit
) {
    val isActive = request.status == RequestStatus.ACTIVE

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Blood Group & Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(if (isActive) Color(0xFFE62129) else Color.Gray, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = request.bloodGroup, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                if (!isActive) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF388E3C), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("COMPLETED", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                } else {
                    Text("ACTIVE", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = request.patientName, fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalHospital, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = request.hospitalName, color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Responses Highlight
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Responses Received", color = Color.DarkGray, fontSize = 14.sp)

                Box(
                    modifier = Modifier
                        .background(
                            color = if (request.responsesCount > 0) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${request.responsesCount} Heroes",
                        color = if (request.responsesCount > 0) Color(0xFF388E3C) else Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // THE UPGRADE: Action to close the request!
            if (isActive) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onMarkCompleted,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5), contentColor = Color(0xFF388E3C)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mark as Completed", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}