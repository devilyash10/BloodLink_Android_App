package com.example.bloodlink.presentation.feature_profile.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.common.CircularAvatar
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDonationHistory: () -> Unit,
    onNavigateToMyRequests: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
    ) {
        CustomTopAppBar(
            title = "Profile",
            onBackClick = onNavigateBack
        )

        // Profile Header Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularAvatar(size = 64.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Rajesh Kumar", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🩸 ", fontSize = 14.sp)
                    Text(text = "O+ Blood Group", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(vertical = 16.dp)
        ) {
            ProfileMenuItem(icon = Icons.Default.PersonOutline, title = "Edit Profile", onClick = { /* TODO */ })
            ProfileMenuItem(icon = Icons.Default.History, title = "Donation History", onClick = onNavigateToDonationHistory)
            ProfileMenuItem(icon = Icons.AutoMirrored.Filled.List, title = "My Requests", onClick = onNavigateToMyRequests)
            ProfileMenuItem(icon = Icons.Default.FavoriteBorder, title = "Saved Donors", onClick = { /* TODO */ })

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp), color = Color(0xFFEEEEEE))

            ProfileMenuItem(icon = Icons.Default.Settings, title = "Settings", onClick = { /* TODO */ })
            ProfileMenuItem(icon = Icons.AutoMirrored.Filled.HelpOutline, title = "Help & Support", onClick = { /* TODO */ })
            ProfileMenuItem(icon = Icons.Default.Info, title = "About Us", onClick = { /* TODO */ })

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            TextButton(
                onClick = onLogoutClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = Color(0xFFE62129))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Logout", color = Color(0xFFE62129), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Forward",
            tint = Color.LightGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        onNavigateBack = {},
        onNavigateToDonationHistory = {},
        onNavigateToMyRequests = {},
        onLogoutClick = {}
    )
}