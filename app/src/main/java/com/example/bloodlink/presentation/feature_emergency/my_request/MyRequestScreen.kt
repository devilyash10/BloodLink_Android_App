package com.example.bloodlink.presentation.feature_emergency.my_request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.domain.model.RequestStatus
import com.example.bloodlink.presentation.components.cards.RequestItemCard
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun MyRequestScreen(
    onNavigateBack: () -> Unit,
    onCreateNewRequest: () -> Unit, // ADDED BACK TO FIX NAVHOST ERROR
    viewModel: MyRequestsViewModel = hiltViewModel(),
    onNavigateToRequestDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val requests by viewModel.filteredRequests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("All", "Active", "Completed")

    // Using Scaffold to easily add a Floating Action Button
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewRequest, // Connects to NavHost to open EmergencyRequestScreen
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
                .padding(paddingValues) // Respect Scaffold padding
                .background(Color(0xFFFAFAFA))
        ) {
            CustomTopAppBar(
                title = "My Requests",
                onBackClick = onNavigateBack
            )

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
                            when(title) {
                                "Active" -> viewModel.filterByStatus(RequestStatus.ACTIVE)
                                "Completed" -> viewModel.filterByStatus(RequestStatus.COMPLETED)
                                else -> viewModel.showAll()
                            }
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

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFE62129))
                }
            } else if (requests.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No requests found.", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(requests) { request ->
                        RequestItemCard(
                            bloodGroup = request.bloodGroup,
                            hospitalName = request.hospitalName,
                            locationArea = request.locationArea,
                            urgencyLevel = request.urgencyLevel.name,
                            timeAgo = request.timeAgo,
                            responsesCount = request.responsesCount,
                            status = request.status.name,
                            onClick = { onNavigateToRequestDetail(request.requestId) } // Pass the requestId to the callback}
                        )
                    }
                }
            }
        }
    }
}