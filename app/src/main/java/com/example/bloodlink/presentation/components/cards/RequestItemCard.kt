package com.example.bloodlink.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RequestItemCard(
    bloodGroup: String,
    hospitalName: String,
    locationArea: String,
    urgencyLevel: String,
    timeAgo: String,
    responsesCount: Int,
    status: String,
    modifier: Modifier = Modifier
) {
    // Dynamically choose colors based on urgency
    val urgencyColor = when (urgencyLevel.uppercase()) {
        "HIGH" -> Color(0xFFE62129) // Red
        "MEDIUM" -> Color(0xFFFFA000) // Orange
        else -> Color(0xFF4CAF50) // Green
    }

    // Dynamically choose colors based on status
    val statusColor = when (status.uppercase()) {
        "ACTIVE" -> Color(0xFF2196F3) // Blue
        "COMPLETED" -> Color(0xFF4CAF50) // Green
        else -> Color.Gray
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Top Row: Blood Group, Hospital Info, Urgency Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Blood Group Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFEBEE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = bloodGroup, color = Color(0xFFE62129), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Hospital Name & Area
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = hospitalName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = locationArea, color = Color.Gray, fontSize = 12.sp)
                }

                // Urgency Badge (With a soft background color)
                Text(
                    text = urgencyLevel,
                    color = urgencyColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(urgencyColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtle Separator Line
            HorizontalDivider(color = Color(0xFFF5F5F5))

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Row: Time, Responses, and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = timeAgo, color = Color.Gray, fontSize = 12.sp)

                // Show responses or "Waiting" text
                Text(
                    text = if (responsesCount > 0) "$responsesCount Donors Responded" else "Waiting for donors",
                    color = if (responsesCount > 0) Color(0xFFE62129) else Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(text = status, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}