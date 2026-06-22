package com.example.bloodlink.presentation.feature_bloodbanks.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MapViewScreen(
    onNavigateBack: () -> Unit,
    onNavigateToList: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToList,
                containerColor = Color.White,
                contentColor = Color(0xFFE62129),
                icon = { Icon(Icons.Default.List, contentDescription = "List View") },
                text = { Text("List View", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE8F0FE)) // Light map-like blue background
        ) {
            // Simulated Map Background Overlay
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Map Pin",
                        tint = Color(0xFFE62129),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Google Maps Integration\nComing Soon",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This view will display pins for all nearby Blood Banks.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Custom Floating Back Button at the Top Left
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .padding(top = 48.dp, start = 16.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .size(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }

            // Search Bar Overlay at the Top
            Card(
                modifier = Modifier
                    .padding(top = 48.dp, start = 80.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
                    Text(text = "Search area...", color = Color.Gray, fontSize = 16.sp)
                }
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun MapViewScreenPreview() {
//    MapViewScreen(onNavigateBack = {})
//}