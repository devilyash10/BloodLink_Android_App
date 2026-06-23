package com.example.bloodlink.presentation.feature_emergency.network_alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.presentation.components.common.CustomTopAppBar
import com.example.bloodlink.presentation.feature_home.RequestItemCard

@Composable
fun NetworkAlertsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRequestDetail: (String) -> Unit,
    viewModel: NetworkAlertsViewModel = hiltViewModel()
) {
    val alerts by viewModel.alerts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        CustomTopAppBar(title = "Network Alerts", onBackClick = onNavigateBack)

        if (isLoading && alerts.isEmpty()) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFF1976D2))
        }

        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                // Enterprise Hospital Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(48.dp).background(Color(0xFFE3F2FD), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Radar, contentDescription = null, tint = Color(0xFF1976D2))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Live Emergency Radar", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                        Text("Monitoring incoming regional requests", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }

            if (alerts.isEmpty() && !isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Text("No active network alerts at this time.", color = Color.Gray)
                    }
                }
            }

            items(alerts) { alert ->
                RequestItemCard(
                    request = alert,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToRequestDetail(alert.requestId) }
                )
            }
        }
    }
}