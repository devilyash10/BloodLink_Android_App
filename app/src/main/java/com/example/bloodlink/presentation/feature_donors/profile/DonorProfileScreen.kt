package com.example.bloodlink.presentation.feature_donors.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloodlink.app.presentation.components.buttons.SecondaryOutlinedButton
import com.example.bloodlink.presentation.components.buttons.PrimaryRedButton
import com.example.bloodlink.presentation.components.common.CircularAvatar

@Composable
fun DonorProfileScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().background(Color.White)
    ) {
        // Curved Red Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    color = Color(0xFFE62129),
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
        ) {
            // Top Bar icons
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                IconButton(onClick = { /* Options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
                }
            }

            // Profile Info
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularAvatar(size = 100.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Rahul Sharma", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStatItem(value = "O+", label = "Blood Group", valueColor = Color(0xFFE62129))
            ProfileStatItem(value = "28", label = "Age", valueColor = Color.Black)
            ProfileStatItem(value = "2.4 KM", label = "Distance", valueColor = Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Availability Status
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "● Available to donate", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), color = Color(0xFFEEEEEE))
        Spacer(modifier = Modifier.height(16.dp))

        // About Details
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "About Donor", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            DetailRow(label = "City", value = "Bangalore")
            DetailRow(label = "Area", value = "Koramangala")
            DetailRow(label = "Last Donation", value = "12 Jan 2024")
            DetailRow(label = "Total Donations", value = "3 times")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SecondaryOutlinedButton(
                text = "Message",
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f)
            )
            PrimaryRedButton(
                text = "Request Blood",
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ProfileStatItem(value: String, label: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = valueColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}