package com.example.bloodlink.presentation.feature_emergency.create_request

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.presentation.components.buttons.PrimaryRedButton
import com.example.bloodlink.presentation.components.common.CustomTopAppBar
import com.example.bloodlink.presentation.components.inputs.StandardTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyRequestScreen(
    onNavigateBack: () -> Unit,
    onSendRequest: () -> Unit,
    viewModel: EmergencyRequestViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // Form States
    var patientName by remember { mutableStateOf("") }
    var hospitalName by remember { mutableStateOf("") }
    var locationArea by remember { mutableStateOf("") }
    var additionalNotes by remember { mutableStateOf("") }

    // Units State
    var unitsRequired by remember { mutableStateOf(1) }

    // Urgency State
    var urgencyLevel by remember { mutableStateOf("HIGH") }

    // Dropdown State
    var bloodGroup by remember { mutableStateOf("O+") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

    // Collect states from the ViewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val requestSuccess by viewModel.requestSuccess.collectAsState()

    // Navigate to the Success Screen when upload finishes
    LaunchedEffect(requestSuccess) {
        if (requestSuccess) {
            onSendRequest()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        CustomTopAppBar(
            title = "Emergency Request",
            onBackClick = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Fill in the details below to notify nearby donors immediately.",
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // --- SECTION 1: BLOOD REQUIREMENT ---
            SectionHeader(title = "Blood Requirement", icon = Icons.Default.Bloodtype, iconColor = Color(0xFFE62129))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Dropdown
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    StandardTextField(
                        value = bloodGroup,
                        onValueChange = {},
                        label = "Blood Group",
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        bloodGroups.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    bloodGroup = selectionOption
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Units Counter
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Units Needed", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = { if (unitsRequired > 1) unitsRequired-- }) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color(0xFFE62129))
                        }
                        Text(text = "$unitsRequired", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { if (unitsRequired < 20) unitsRequired++ }) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color(0xFFE62129))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECTION 2: PATIENT & URGENCY ---
            SectionHeader(title = "Patient Details", icon = Icons.Default.Person, iconColor = Color(0xFF1976D2))

            StandardTextField(
                value = patientName,
                onValueChange = { patientName = it },
                label = "Patient Name (Required)",
                leadingIcon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Urgency Level", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            // Custom Ultimate Urgency Selector
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                UrgencyOption(
                    title = "NORMAL",
                    isSelected = urgencyLevel == "NORMAL",
                    selectedColor = Color(0xFF388E3C),
                    modifier = Modifier.weight(1f),
                    onClick = { urgencyLevel = "NORMAL" }
                )
                UrgencyOption(
                    title = "HIGH",
                    isSelected = urgencyLevel == "HIGH",
                    selectedColor = Color(0xFFF57C00),
                    modifier = Modifier.weight(1f),
                    onClick = { urgencyLevel = "HIGH" }
                )
                UrgencyOption(
                    title = "CRITICAL",
                    isSelected = urgencyLevel == "CRITICAL",
                    selectedColor = Color(0xFFD32F2F),
                    modifier = Modifier.weight(1f),
                    onClick = { urgencyLevel = "CRITICAL" }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECTION 3: LOCATION ---
            SectionHeader(title = "Hospital Location", icon = Icons.Default.LocalHospital, iconColor = Color(0xFF00796B))

            StandardTextField(
                value = hospitalName,
                onValueChange = { hospitalName = it },
                label = "Hospital Name",
                leadingIcon = Icons.Default.LocalHospital
            )

            Spacer(modifier = Modifier.height(16.dp))

            StandardTextField(
                value = locationArea,
                onValueChange = { locationArea = it },
                label = "City / Area (e.g., MG Road, Bangalore)",
                leadingIcon = Icons.Default.LocationCity,
                trailingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = "Use my location", tint = Color(0xFFE62129))
                }
            )
            Text(text = "Auto-detect location", color = Color(0xFFE62129), fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 4.dp, start = 8.dp).clickable { /* TODO */ })

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECTION 4: ADDITIONAL INFO ---
            Text(text = "Additional Notes (Optional)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            StandardTextField(
                value = additionalNotes,
                onValueChange = { additionalNotes = it },
                label = "Any specific instructions or contact info..."
            )

            // Display error messages from the ViewModel if any
            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = it, color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- SUBMIT BUTTON ---
            PrimaryRedButton(
                text = if (isLoading) "Sending Request..." else "Send Emergency Request",
                onClick = {
                    if (!isLoading) {
                        viewModel.submitEmergencyRequest(
                            patientName = patientName,
                            bloodGroup = bloodGroup,
                            hospitalName = hospitalName,
                            locationArea = locationArea,
                            urgencyLevel = urgencyLevel,
                            units = unitsRequired.toString(), // Automatically converts our nice counter to String for the ViewModel
                            notes = additionalNotes
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
        Box(
            modifier = Modifier.size(32.dp).background(iconColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun UrgencyOption(
    title: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) selectedColor else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) selectedColor else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}