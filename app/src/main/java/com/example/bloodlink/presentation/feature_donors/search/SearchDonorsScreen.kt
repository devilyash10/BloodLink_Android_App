package com.example.bloodlink.presentation.feature_donors.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.common.CustomTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDonorsScreen(
    onNavigateBack: () -> Unit,
    // This perfectly matches your AppNavHost implementation!
    onSearchClicked: (bloodGroup: String, query: String, distance: Float) -> Unit
) {
    // State Variables
    var selectedBloodGroup by remember { mutableStateOf("All") }
    var locationQuery by remember { mutableStateOf("") }
    var searchRadius by remember { mutableFloatStateOf(10f) }
    var isCritical by remember { mutableStateOf(false) }

    val bloodGroups = listOf("All", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        CustomTopAppBar(title = "Donor Radar", onBackClick = onNavigateBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- HEADER ---
            Text(text = "Find a Lifesaver", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Adjust the radar parameters to find eligible blood donors near your location.", color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)

            Spacer(modifier = Modifier.height(32.dp))

            // --- BLOOD GROUP GRID ---
            Text(text = "Select Blood Group", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(180.dp) // Fixed height for grid
            ) {
                items(bloodGroups) { group ->
                    BloodGroupChip(
                        text = group,
                        isSelected = selectedBloodGroup == group,
                        onClick = { selectedBloodGroup = group }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- LOCATION INPUT ---
            Text(text = "Search Area (City/Pin)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = locationQuery,
                onValueChange = { locationQuery = it },
                placeholder = { Text("e.g. Bhopal, 462001", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFE62129)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFFE62129),
                    unfocusedIndicatorColor = Color(0xFFE0E0E0)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- DYNAMIC RADIUS SLIDER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Search Radius", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "${searchRadius.toInt()} km", fontWeight = FontWeight.Bold, color = Color(0xFFE62129))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = searchRadius,
                onValueChange = { searchRadius = it },
                valueRange = 5f..50f,
                steps = 8, // 5, 10, 15... 50
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFE62129),
                    activeTrackColor = Color(0xFFE62129),
                    inactiveTrackColor = Color(0xFFFFEBEE)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- URGENCY TOGGLE ---
            Card(
                colors = CardDefaults.cardColors(containerColor = if (isCritical) Color(0xFFFFEBEE) else Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, if (isCritical) Color(0xFFE62129).copy(alpha = 0.5f) else Color(0xFFEEEEEE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).background(if (isCritical) Color(0xFFE62129) else Color(0xFFF5F5F5), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CrisisAlert, contentDescription = null, tint = if (isCritical) Color.White else Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Critical Emergency", fontWeight = FontWeight.Bold, color = if (isCritical) Color(0xFFD32F2F) else Color.Black)
                            Text("Bypass standard filters", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Switch(
                        checked = isCritical,
                        onCheckedChange = { isCritical = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFE62129))
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- MASSIVE ACTIVATE BUTTON ---
            Button(
                onClick = { onSearchClicked(selectedBloodGroup, locationQuery, searchRadius) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129)),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp)
            ) {
                Icon(Icons.Default.Radar, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Activate Radar Search", fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(48.dp)) // Bottom padding
        }
    }
}

// Custom Premium Chip Component
@Composable
fun BloodGroupChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFFE62129) else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.DarkGray,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}