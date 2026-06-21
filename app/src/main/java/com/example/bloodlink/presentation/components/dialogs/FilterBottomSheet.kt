package com.example.bloodlink.presentation.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.inputs.BloodGroupSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismissRequest: () -> Unit,
    onApplyFilters: (bloodGroup: String, distance: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedGroup by remember { mutableStateOf("All") }
    var distance by remember { mutableStateOf(10f) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = Color.White
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text("Advanced Filters", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(24.dp))

            Text("Blood Group", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            BloodGroupSelector(
                selectedGroup = selectedGroup,
                onGroupSelected = { selectedGroup = it },
                includeAllOption = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Distance", fontWeight = FontWeight.SemiBold)
                Text("${distance.toInt()} km", color = Color(0xFFE62129), fontWeight = FontWeight.Bold)
            }
            Slider(
                value = distance,
                onValueChange = { distance = it },
                valueRange = 1f..50f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFE62129),
                    activeTrackColor = Color(0xFFE62129)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onApplyFilters(selectedGroup, distance) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129))
            ) {
                Text("Apply Filters")
            }
            Spacer(modifier = Modifier.height(16.dp)) // Bottom padding for navigation bar spacing
        }
    }
}