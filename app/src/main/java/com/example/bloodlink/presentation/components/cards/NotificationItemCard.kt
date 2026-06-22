package com.example.bloodlink.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.domain.model.NotificationType

@Composable
fun NotificationItemCard(
    title: String,
    message: String,
    timeAgo: String,
    isRead: Boolean,
    type: NotificationType,
    modifier: Modifier = Modifier
) {
    val icon = when (type) {
        NotificationType.URGENT_REQUEST -> Icons.Default.Warning
        NotificationType.DONOR_ACCEPTED -> Icons.Default.CheckCircle
        NotificationType.SYSTEM_UPDATE -> Icons.Default.Info
    }

    val iconTint = when (type) {
        NotificationType.URGENT_REQUEST -> Color(0xFFE62129) // Red
        NotificationType.DONOR_ACCEPTED -> Color(0xFF4CAF50) // Green
        NotificationType.SYSTEM_UPDATE -> Color(0xFF2196F3)  // Blue
    }

    val iconBackground = iconTint.copy(alpha = 0.1f)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRead) Color.White else Color(0xFFFFF5F5) // Light red tint if unread
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isRead) 1.dp else 3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Icon Badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Content
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontWeight = if (isRead) FontWeight.SemiBold else FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(text = timeAgo, color = Color.Gray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}