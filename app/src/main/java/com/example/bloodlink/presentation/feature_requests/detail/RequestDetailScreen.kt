package com.example.bloodlink.presentation.feature_requests.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.domain.model.UrgencyLevel
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun RequestDetailScreen(
    requestId: String,
    onNavigateBack: () -> Unit,
    viewModel: RequestDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val request by viewModel.requestDetail.collectAsState()
    val isOwnRequest by viewModel.isOwnRequest.collectAsState()
    val requesterProfile by viewModel.requesterProfile.collectAsState()
    val respondingHeroes by viewModel.respondingHeroes.collectAsState()
    val actionSuccess by viewModel.actionSuccess.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val showIncompatibleDialog by viewModel.showIncompatibleDialog.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .systemBarsPadding()
    ) {
        CustomTopAppBar(title = "Request Details", onBackClick = onNavigateBack)

        if (isLoading && request == null) {
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

                // --- LOGIC SPLIT: MY REQUEST vs SOMEONE ELSE'S REQUEST ---

                if (isOwnRequest) {
                    // ====== VIEWING MY OWN REQUEST ======
                    Text(text = "Heroes Responding (${respondingHeroes.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (respondingHeroes.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Text(
                                "Waiting for donors to respond...",
                                modifier = Modifier.padding(16.dp),
                                color = Color.Gray
                            )
                        }
                    } else {
                        respondingHeroes.forEach { hero ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = hero.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Blood: ${hero.bloodGroup}", color = Color.Gray, fontSize = 14.sp)
                                    }
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${hero.phoneNumber}") }
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier.background(Color(0xFFE8F5E9), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = "Call", tint = Color(0xFF388E3C))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(40.dp))

                    // Mark as Completed Button
                    Button(
                        onClick = { viewModel.markAsCompleted() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = !actionSuccess,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF388E3C), // Green
                            disabledContainerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (actionSuccess) "Request Completed" else "Mark as Received", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                } else {
                    // ====== VIEWING SOMEONE ELSE'S REQUEST ======
                    requesterProfile?.let { profile ->
                        Text(text = "Requested By", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = profile.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = profile.phoneNumber, color = Color.Gray, fontSize = 14.sp)
                                }
                                IconButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${profile.phoneNumber}") }
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.background(Color(0xFFE8F5E9), CircleShape)
                                ) {
                                    Icon(Icons.Default.Phone, contentDescription = "Call", tint = Color(0xFF388E3C))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(40.dp))

                    // I Can Donate Button
                    Button(
                        onClick = { viewModel.acceptRequest() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = !actionSuccess,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (actionSuccess) Color(0xFF4CAF50) else Color(0xFFE62129),
                            disabledContainerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Bloodtype, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (actionSuccess) "Request Accepted!" else "I Can Donate", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    // --- THE INCOMPATIBILITY WARNING DIALOG ---
    if (showIncompatibleDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialog() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Medical Incompatibility", fontWeight = FontWeight.Bold, color = Color.Red)
                }
            },
            text = {
                Text(
                    text = "Your blood group is ${viewModel.currentUserProfile?.bloodGroup}. " +
                            "Biologically, you cannot donate to a patient needing ${request?.bloodGroup}. \n\n" +
                            "Please let a compatible donor answer this request.",
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissDialog() }) {
                    Text("Understood", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}