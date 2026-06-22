package com.example.bloodlink.presentation.feature_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AppSettingsViewModel = hiltViewModel(), // Using the new ViewModel!
    modifier: Modifier = Modifier
) {
    val pushEnabled by viewModel.pushNotifications.collectAsState()
    val locationEnabled by viewModel.locationServices.collectAsState()
    val darkEnabled by viewModel.darkMode.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        CustomTopAppBar(
            title = "App Settings",
            onBackClick = onNavigateBack
        )

        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {

            // Notifications Section
            Text(text = "Notifications", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            SettingsToggleItem(
                icon = Icons.Default.Notifications,
                title = "Push Notifications",
                isChecked = pushEnabled,
                onCheckedChange = { viewModel.togglePushNotifications(it) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // App Preferences Section
            Text(text = "Preferences", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            SettingsToggleItem(
                icon = Icons.Default.LocationOn,
                title = "Location Services",
                isChecked = locationEnabled,
                onCheckedChange = { viewModel.toggleLocationServices(it) }
            )
            SettingsToggleItem(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                isChecked = darkEnabled,
                onCheckedChange = { viewModel.toggleDarkMode(it) }
            )
        }
    }
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFE62129)
            )
        )
    }
}