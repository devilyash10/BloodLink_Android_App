package com.example.bloodlink.presentation.feature_bloodbanks.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.domain.model.BloodBank
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BloodBanksScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMap: () -> Unit, // Added Navigation for the Map
    viewModel: BloodBanksViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val bloodBanks by viewModel.filteredBloodBanks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToMap,
                containerColor = Color(0xFFE62129),
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Map, contentDescription = "Map View") },
                text = { Text("Map View", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA))
        ) {
            CustomTopAppBar(title = "Blood Banks", onBackClick = onNavigateBack)

            // Search Bar
            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search by name or area...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE62129),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFE62129))
                }
            } else if (bloodBanks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No blood banks found.", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bloodBanks) { bank ->
                        ImprovedBloodBankCard(bloodBank = bank)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImprovedBloodBankCard(bloodBank: BloodBank) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Flat design
        shape = RoundedCornerShape(16.dp),
        border = border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp)) // Subtle border
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Icon, Name, and Status
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFFFEBEE), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.LocalHospital, contentDescription = null, tint = Color(0xFFE62129), modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = bloodBank.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = bloodBank.address, color = Color.Gray, fontSize = 13.sp, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (bloodBank.isOpen) Color(0xFF4CAF50) else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (bloodBank.isOpen) "Open Now" else "Closed",
                            color = if (bloodBank.isOpen) Color(0xFF4CAF50) else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(text = "  •  ${bloodBank.distanceKm} km", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Available Stock", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                bloodBank.availableBloodGroups.forEach { group ->
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color(0xFFE62129).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = group, color = Color(0xFFE62129), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray),
                    border = border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Call", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Directions", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Helper for borders since ButtonDefaults.outlinedButtonBorder can be finicky
fun border(width: androidx.compose.ui.unit.Dp, color: Color, shape: androidx.compose.ui.graphics.Shape) =
    androidx.compose.foundation.BorderStroke(width, color)
//
//@Preview(showBackground = true)
//@Composable
//fun BloodBanksScreenPreview() {
//    BloodBanksScreen(onNavigateBack = {})
//}