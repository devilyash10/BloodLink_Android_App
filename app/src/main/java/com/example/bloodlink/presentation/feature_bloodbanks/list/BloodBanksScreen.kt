package com.example.bloodlink.presentation.feature_bloodbanks.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun BloodBanksScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFFFAFAFA))
    ) {
        CustomTopAppBar(
            title = "Nearby Blood Banks",
            onBackClick = onNavigateBack
        )

        // Map Placeholder Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFE0E0E0)), // Gray placeholder for the Google Map
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Map, contentDescription = "Map View", tint = Color.Gray, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Google Maps Integration", color = Color.DarkGray)
            }
        }

        // List of Blood Banks
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BloodBankListItem(
                    name = "Red Cross Blood Bank",
                    distance = "1.2 KM",
                    area = "Koramangala",
                    isOpen = true,
                    timeInfo = "Closes 8 PM"
                )
            }
            item {
                BloodBankListItem(
                    name = "Jeevan Blood Bank",
                    distance = "2.5 KM",
                    area = "HSR Layout",
                    isOpen = true,
                    timeInfo = "Closes 7 PM"
                )
            }
            item {
                BloodBankListItem(
                    name = "Narayana Blood Bank",
                    distance = "3.8 KM",
                    area = "BTM Layout",
                    isOpen = true,
                    timeInfo = "Closes 8 PM"
                )
            }
        }
    }
}

// Specialized List Item component to match this specific screen's design
@Composable
fun BloodBankListItem(
    name: String,
    distance: String,
    area: String,
    isOpen: Boolean,
    timeInfo: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location Pin Icon Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFFFEBEE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFE62129))
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "$distance • $area", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = if (isOpen) "Open " else "Closed ",
                        color = if (isOpen) Color(0xFF4CAF50) else Color(0xFFE62129),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "• $timeInfo", color = Color.Gray, fontSize = 12.sp)
                }
            }

            // Call Button
            IconButton(onClick = { /* TODO: Launch Phone Intent */ }) {
                Icon(Icons.Default.Phone, contentDescription = "Call", tint = Color(0xFFE62129))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BloodBanksScreenPreview() {
    BloodBanksScreen(onNavigateBack = {})
}