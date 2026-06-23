package com.example.bloodlink.presentation.feature_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        CustomTopAppBar(
            title = "Settings",
            onBackClick = onNavigateBack
        )

        Column(modifier = Modifier.padding(24.dp)) {

            Text(text = "General Settings", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            SettingsMenuItem(icon = Icons.Default.Notifications, title = "Push Notifications")
            SettingsMenuItem(icon = Icons.Default.PrivacyTip, title = "Privacy Policy")
            SettingsMenuItem(icon = Icons.Default.Info, title = "Terms of Service")

            // Note: You can add Theme (Dark/Light mode) toggles here in the future!
        }
    }
}

@Composable
fun SettingsMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
        }
    }
}