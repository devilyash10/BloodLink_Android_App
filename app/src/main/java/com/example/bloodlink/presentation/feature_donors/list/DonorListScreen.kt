package com.example.bloodlink.presentation.feature_donors.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.presentation.components.cards.DonorListItemCard
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun DonorListScreen(
    onNavigateBack: () -> Unit,
    onDonorClick: (String) -> Unit, // Pass donor ID
    viewModel: DonorListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val donors by viewModel.donorList.collectAsState()

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
            // This is the preferred way to iterate over lists in LazyColumn
            items(donors) { donor ->
                DonorListItemCard(
                    name = donor.fullName,
                    bloodGroup = donor.bloodGroup,
                    distanceKm = donor.distanceKm.toString(),
                    isAvailable = donor.isAvailableAsDonor,
                    onClick = { onDonorClick(donor.id) }
                )
            }
        }
    }
}