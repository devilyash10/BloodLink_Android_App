package com.example.bloodlink.presentation.feature_emergency.my_requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.cards.RequestItemCard
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun MyRequestsScreen(
    onNavigateBack: () -> Unit,
    onCreateNewRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Active", "Completed", "Cancelled")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFAFAFA),
        topBar = {
            CustomTopAppBar(
                title = "My Requests",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewRequest,
                containerColor = Color(0xFFE62129),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Request")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Custom Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Color(0xFFE62129),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color(0xFFE62129),
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                color = if (isSelected) Color(0xFFE62129) else Color.Gray,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List of Requests based on Tab
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedTabIndex == 0) { // Active Tab
                    item {
                        RequestItemCard(
                            hospitalName = "Apollo Hospital, Bangalore",
                            bloodGroup = "O+",
                            urgency = "High",
                            timeAgo = "2 min ago"
                        )
                    }
                    item {
                        RequestItemCard(
                            hospitalName = "Fortis Hospital, Bangalore",
                            bloodGroup = "B+",
                            urgency = "Medium",
                            timeAgo = "1 day ago"
                        )
                    }
                } else if (selectedTabIndex == 1) { // Completed Tab
                    item {
                        RequestItemCard(
                            hospitalName = "Manipal Hospital, Bangalore",
                            bloodGroup = "A+",
                            urgency = "Low",
                            timeAgo = "2 days ago"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyRequestsScreenPreview() {
    MyRequestsScreen(onNavigateBack = {}, onCreateNewRequest = {})
}