package com.example.bloodlink.presentation.feature_donors.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.cards.DonorListItemCard
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun DonorListScreen(
    onNavigateBack: () -> Unit,
    onDonorClick: (String) -> Unit, // Pass donor ID
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFFFAFAFA))
    ) {
        CustomTopAppBar(
            title = "Nearby Donors",
            onBackClick = onNavigateBack,
            actions = {
                IconButton(onClick = { /* Open Filters */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filters")
                }
            },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Subtitle specific to this screen design
        Text(
            text = "O+ • Within 10 KM",
            color = Color(0xFFE62129),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 24.dp).offset(y = (-16).dp)
        )

        Text(
            text = "12 donors found",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // In reality, this will be a items(donorList) loop
            item {
                DonorListItemCard(name = "Rahul Sharma", bloodGroup = "O+", distanceKm = "2.4", isAvailable = true, onClick = { onDonorClick("1") })
            }
            item {
                DonorListItemCard(name = "Ankita Patel", bloodGroup = "O+", distanceKm = "3.1", isAvailable = true, onClick = { onDonorClick("2") })
            }
            item {
                DonorListItemCard(name = "Amit Verma", bloodGroup = "O+", distanceKm = "4.5", isAvailable = true, onClick = { onDonorClick("3") })
            }
            item {
                DonorListItemCard(name = "Neha Singh", bloodGroup = "O+", distanceKm = "5.2", isAvailable = false, onClick = { onDonorClick("4") })
            }
            item {
                DonorListItemCard(name = "Vikram Reddy", bloodGroup = "O+", distanceKm = "6.8", isAvailable = true, onClick = { onDonorClick("5") })
            }
        }
    }
}