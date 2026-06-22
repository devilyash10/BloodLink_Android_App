package com.example.bloodlink.presentation.feature_requests.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.domain.model.UrgencyLevel
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun RequestDetailScreen(
    requestId: String, // We keep this parameter just in case, but the ViewModel does the heavy lifting now
    onNavigateBack: () -> Unit,
    viewModel: RequestDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val request by viewModel.requestDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .systemBarsPadding()
    ) {
        CustomTopAppBar(title = "Request Details", onBackClick = onNavigateBack)

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFE62129))
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage!!, color = Color.Red, fontWeight = FontWeight.Medium)
            }
        } else if (request != null) {
            val req = request!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // --- Top Header: Blood Group & Urgency ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Your elegant circular badge
                        Box(
                            modifier = Modifier.size(72.dp).background(Color(0xFFFFEBEE), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = req.bloodGroup, color = Color(0xFFE62129), fontWeight = FontWeight.Black, fontSize = 26.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Blood Required", color = Color.Gray, fontSize = 14.sp)
                            Text(text = "${req.unitsRequired} Units", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }

                    // Urgency Badge
                    val urgencyColor = when(req.urgencyLevel) {
                        UrgencyLevel.CRITICAL -> Color(0xFFD32F2F)
                        UrgencyLevel.HIGH -> Color(0xFFF57C00)
                        else -> Color(0xFF388E3C)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = urgencyColor, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = req.urgencyLevel.name, color = urgencyColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Patient & Location Details Card ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFE62129), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Patient Name", color = Color.Gray, fontSize = 13.sp)
                                Text(req.patientName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF5F5F5))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalHospital, contentDescription = null, tint = Color(0xFFE62129), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Hospital", color = Color.Gray, fontSize = 13.sp)
                                Text(req.hospitalName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF5F5F5))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Area", color = Color.Gray, fontSize = 13.sp)
                                Text(req.locationArea.ifBlank { "Location not specified" }, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Additional Notes (Only shows if there are notes) ---
                if (req.additionalNotes.isNotBlank()) {
                    Text(text = "Additional Notes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F8)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFEBEE))
                    ) {
                        Text(
                            text = req.additionalNotes,
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFF5D4037),
                            lineHeight = 22.sp,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(40.dp))

                // --- Huge Action Button ---
                Button(
                    onClick = { /* TODO: Trigger donor acceptance pipeline */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Default.Bloodtype, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("I Can Donate", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}