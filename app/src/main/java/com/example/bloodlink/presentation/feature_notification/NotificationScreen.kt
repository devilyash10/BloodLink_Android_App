package com.example.bloodlink.presentation.feature_notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.common.CircularAvatar
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().background(Color.White)
    ) {
        CustomTopAppBar(
            title = "Notifications",
            onBackClick = onNavigateBack,
            actions = {
                TextButton(onClick = { /* Mark all read logic */ }) {
                    Text(text = "Mark all as read", color = Color(0xFFE62129), fontWeight = FontWeight.SemiBold)
                }
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
        ) {
            item {
                NotificationItem(
                    title = "New Request Near You",
                    description = "O+ blood request in your area",
                    timeAgo = "2 min ago",
                    icon = Icons.Default.Bloodtype,
                    iconTint = Color(0xFFE62129),
                    iconBgColor = Color(0xFFFFEBEE),
                    isUnread = true
                )
            }
            item {
                NotificationItem(
                    title = "Rahul Sharma Accepted",
                    description = "Your request has been accepted",
                    timeAgo = "10 min ago",
                    isAvatar = true, // Uses Avatar instead of Icon
                    isUnread = false
                )
            }
            item {
                NotificationItem(
                    title = "Donation Reminder",
                    description = "You can donate blood after 20 Apr 2024",
                    timeAgo = "1 day ago",
                    icon = Icons.Default.Notifications,
                    iconTint = Color(0xFF9C27B0),
                    iconBgColor = Color(0xFFF3E5F5),
                    isUnread = false
                )
            }
            item {
                NotificationItem(
                    title = "Thank You!",
                    description = "You helped save a life.",
                    timeAgo = "2 days ago",
                    icon = Icons.Default.Favorite,
                    iconTint = Color(0xFF4CAF50),
                    iconBgColor = Color(0xFFE8F5E9),
                    isUnread = false
                )
            }
            item {
                NotificationItem(
                    title = "System Update",
                    description = "New features are added",
                    timeAgo = "3 days ago",
                    icon = Icons.Default.Update,
                    iconTint = Color(0xFF2196F3),
                    iconBgColor = Color(0xFFE3F2FD),
                    isUnread = false
                )
            }
        }
    }
}

@Composable
fun NotificationItem(
    title: String,
    description: String,
    timeAgo: String,
    icon: ImageVector? = null,
    iconTint: Color = Color.Gray,
    iconBgColor: Color = Color.LightGray,
    isAvatar: Boolean = false,
    isUnread: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isUnread) Color(0xFFFFF8F8) else Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading Icon or Avatar
            if (isAvatar) {
                CircularAvatar(size = 48.dp)
            } else {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (icon != null) {
                        Icon(imageVector = icon, contentDescription = null, tint = iconTint)
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Trailing Data (Time & Unread Dot)
            Column(horizontalAlignment = Alignment.End) {
                Text(text = timeAgo, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                if (isUnread) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFFE62129), CircleShape)
                    )
                }
            }
        }
        HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(start = 88.dp, end = 24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen(onNavigateBack = {})
}