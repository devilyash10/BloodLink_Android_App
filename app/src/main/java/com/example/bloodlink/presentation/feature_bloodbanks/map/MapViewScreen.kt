package com.example.bloodlink.presentation.feature_bloodbanks.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
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
import com.example.bloodlink.presentation.components.common.CircularAvatar

@Composable
fun MapViewScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().background(Color(0xFFE0E0E0)) // Base map placeholder color
    ) {
        // --- Map Placeholder Content ---
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
            Text("Google Maps Integration", color = Color.DarkGray, fontWeight = FontWeight.SemiBold)
        }

        // Mocking pins on the map
        Box(modifier = Modifier.offset(x = 100.dp, y = 250.dp).size(24.dp).background(Color(0xFFE62129), CircleShape).align(Alignment.TopStart))
        Box(modifier = Modifier.offset(x = (-80).dp, y = 300.dp).size(24.dp).background(Color(0xFF2196F3), CircleShape).align(Alignment.TopEnd))
        // -------------------------------

        // Floating Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.background(Color.White, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Donors Near You", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Text("O+ • Within 10 KM", fontSize = 12.sp, color = Color.DarkGray)
            }
        }

        // Selected Pin Card (Bottom Sheet Equivalent)
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularAvatar(size = 48.dp)
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Rahul Sharma", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("2.4 KM Away", color = Color.Gray, fontSize = 12.sp)
                    Text("● Available", color = Color(0xFF4CAF50), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(
                        onClick = { /* Call */ },
                        modifier = Modifier.background(Color(0xFFFFEBEE), CircleShape).size(40.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Call", tint = Color(0xFFE62129), modifier = Modifier.size(20.dp))
                    }
                    IconButton(
                        onClick = { /* Message */ },
                        modifier = Modifier.background(Color(0xFFFFEBEE), CircleShape).size(40.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "Message", tint = Color(0xFFE62129), modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapViewScreenPreview() {
    MapViewScreen(onNavigateBack = {})
}