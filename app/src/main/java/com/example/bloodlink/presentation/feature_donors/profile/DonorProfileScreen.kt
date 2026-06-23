package com.example.bloodlink.presentation.feature_donors.profile

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.domain.util.BloodCompatibility
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun DonorProfileScreen(
    donorId: String,
    onNavigateBack: () -> Unit,
    viewModel: DonorProfileViewModel = hiltViewModel()
) {
    val donorData by viewModel.donorData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    // Dialog State
    var showRequestDialog by remember { mutableStateOf(false) }
    var selectedPatientGroup by remember { mutableStateOf("") }
    var compatibilityError by remember { mutableStateOf<String?>(null) }

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        Column(modifier = Modifier.fillMaxSize()) {
            CustomTopAppBar(title = "Profile", onBackClick = onNavigateBack)

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFFE62129))
            }

            donorData?.let { donor ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- 1. HERO AVATAR & NAME ---
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(Color.White, CircleShape)
                            .border(4.dp, Color(0xFFE62129).copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = donor.bloodGroup, color = Color(0xFFE62129), fontWeight = FontWeight.ExtraBold, fontSize = 34.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = donor.name, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = Color(0xFF2196F3), modifier = Modifier.size(22.dp))
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = donor.status, color = if (donor.status.contains("Available")) Color(0xFF388E3C) else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- 2. DEMOGRAPHIC TAGS (Pill Shaped) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        DemographicChip(icon = Icons.Default.Person, text = "${donor.age}, ${donor.gender}")
                        Spacer(modifier = Modifier.width(12.dp))
                        DemographicChip(icon = Icons.Default.LocationOn, text = "${donor.area}, ${donor.city}")
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- 3. IMPACT DASHBOARD ---
                    Row(
                        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp)).padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStatItem("Donations", "${donor.totalDonations}", Color(0xFFE62129))
                        ProfileStatItem("Distance", donor.distance.replace(" km away", "km"), Color(0xFF1976D2))
                        ProfileStatItem("Last Donated", donor.lastDonated, Color(0xFF388E3C))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- 4. HEALTH SUMMARY CARD ---
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Health Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(16.dp))
                            MedicalDetailItem(icon = Icons.Default.MedicalServices, title = "Current Medication", value = donor.medication, tint = Color(0xFFF57C00))
                            Spacer(modifier = Modifier.height(16.dp))
                            MedicalDetailItem(icon = Icons.Default.FavoriteBorder, title = "Medical Conditions", value = donor.conditions, tint = Color(0xFFE62129))
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp)) // Padding for bottom bar
                }
            }
        }

        // --- 5. STICKY BOTTOM ACTION BAR ---
        donorData?.let { donor ->
            val safePhone = donor.phoneNumber.ifBlank { "0000000000" }

            Surface(
                color = Color.White,
                shadowElevation = 16.dp,
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Call Icon
                    OutlinedIconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:$safePhone") }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(54.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = Color(0xFF388E3C))
                    }

                    // Message Icon
                    OutlinedIconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply { data = Uri.parse("smsto:$safePhone") }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(54.dp),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Icon(Icons.Default.Message, contentDescription = "Message", tint = Color(0xFF1976D2))
                    }

                    // Request Blood Button (Primary)
                    Button(
                        onClick = { showRequestDialog = true },
                        modifier = Modifier.weight(1f).height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.WaterDrop, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Request Blood", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // --- 6. PRE-VERIFICATION DIALOG ---
    if (showRequestDialog && donorData != null) {
        AlertDialog(
            onDismissRequest = {
                showRequestDialog = false
                compatibilityError = null
                selectedPatientGroup = ""
            },
            containerColor = Color.White,
            title = {
                Text("Verify Compatibility", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("Please select the patient's blood group to ensure this donor is a match.", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(120.dp)
                    ) {
                        items(bloodGroups) { group ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedPatientGroup == group) Color(0xFFE62129) else Color(0xFFF5F5F5))
                                    .clickable {
                                        selectedPatientGroup = group
                                        compatibilityError = null
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = group,
                                    color = if (selectedPatientGroup == group) Color.White else Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    if (compatibilityError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = compatibilityError!!, color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedPatientGroup.isEmpty()) {
                            compatibilityError = "Please select a blood group."
                            return@Button
                        }

                        // Engine Check! Can Donor donate to Patient?
                        val isMatch = BloodCompatibility.canDonate(donorData!!.bloodGroup, selectedPatientGroup)

                        if (isMatch) {
                            showRequestDialog = false
                            Toast.makeText(context, "Match Confirmed! Request Sent.", Toast.LENGTH_SHORT).show()
                            // TODO: Add actual request sending logic to ViewModel here later
                        } else {
                            compatibilityError = "Medical mismatch. ${donorData!!.bloodGroup} cannot donate to $selectedPatientGroup."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129))
                ) {
                    Text("Check & Send")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRequestDialog = false
                    compatibilityError = null
                }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

// --- HELPER COMPOSABLES ---

@Composable
fun DemographicChip(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(Color(0xFFF5F5F5), RoundedCornerShape(20.dp)).padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, color = Color.DarkGray, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProfileStatItem(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = valueColor)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MedicalDetailItem(icon: ImageVector, title: String, value: String, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.size(44.dp).background(tint.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}