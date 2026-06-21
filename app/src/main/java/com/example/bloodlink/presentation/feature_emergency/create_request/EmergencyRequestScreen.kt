package com.example.bloodlink.presentation.feature_emergency.create_request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.buttons.PrimaryRedButton
import com.example.bloodlink.presentation.components.common.CustomTopAppBar
import com.example.bloodlink.presentation.components.common.UrgencyToggle
import com.example.bloodlink.presentation.components.inputs.StandardTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyRequestScreen(
    onNavigateBack: () -> Unit,
    onSendRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var hospitalLocation by remember { mutableStateOf("Apollo Hospital, Bangalore") }
    var patientName by remember { mutableStateOf("Ramesh Kumar") }
    var additionalNotes by remember { mutableStateOf("Need immediately for surgery.") }
    var urgencyLevel by remember { mutableStateOf("High") }

    // Dropdown State
    var bloodGroup by remember { mutableStateOf("O+") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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
            Text(
                text = "Send request to nearby donors",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Blood Group Dropdown
            Text(text = "Blood Group Needed", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                StandardTextField(
                    value = bloodGroup,
                    onValueChange = {},
                    label = "Select Blood Group",
                    readOnly = true,
                    leadingIcon = Icons.Default.LocalHospital, // Using hospital cross icon
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

            Spacer(modifier = Modifier.height(24.dp))

            // Hospital Location
            Text(text = "Hospital / Location", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            StandardTextField(
                value = hospitalLocation,
                onValueChange = { hospitalLocation = it },
                label = "Enter hospital name",
                trailingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = "Use my location", tint = Color(0xFFE62129))
                }
            )
            Text(text = "Use my location", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp, start = 8.dp))

            Spacer(modifier = Modifier.height(24.dp))

            // Patient Name
            Text(text = "Patient Name (Optional)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            StandardTextField(
                value = patientName,
                onValueChange = { patientName = it },
                label = "Enter patient name",
                leadingIcon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Urgency Toggle
            Text(text = "Urgency Level", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            UrgencyToggle(
                selectedLevel = urgencyLevel,
                onLevelSelected = { urgencyLevel = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Additional Notes
            Text(text = "Additional Notes (Optional)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            StandardTextField(
                value = additionalNotes,
                onValueChange = { additionalNotes = it },
                label = "Any specific instructions"
            )

            Spacer(modifier = Modifier.height(40.dp))

            PrimaryRedButton(
                text = "Send Request",
                onClick = onSendRequest,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmergencyRequestScreenPreview() {
    EmergencyRequestScreen(onNavigateBack = {}, onSendRequest = {})
}