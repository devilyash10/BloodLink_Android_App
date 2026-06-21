package com.example.bloodlink.presentation.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BloodGroupSelector(
    selectedGroup: String,
    onGroupSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    includeAllOption: Boolean = false
) {
    val bloodGroups = mutableListOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
    if (includeAllOption) bloodGroups.add(0, "All")

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        bloodGroups.forEach { group ->
            val isSelected = selectedGroup == group
            Surface(
                onClick = { onGroupSelected(group) },
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) Color(0xFFE62129) else Color.Transparent,
                border = BorderStroke(1.dp, if (isSelected) Color(0xFFE62129) else Color.LightGray)
            ) {
                Text(
                    text = group,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}