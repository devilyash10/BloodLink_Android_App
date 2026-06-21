package com.example.bloodlink.presentation.components.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun UrgencyToggle(
    selectedLevel: String,
    onLevelSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val levels = listOf("Low", "Medium", "High")

    Row(modifier = modifier.fillMaxWidth()) {
        levels.forEachIndexed { index, level ->
            val isSelected = selectedLevel == level

            // Handle corner rounding for the segmented look
            val shape = when (index) {
                0 -> RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                levels.lastIndex -> RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                else -> RoundedCornerShape(0.dp)
            }

            Surface(
                modifier = Modifier.weight(1f).height(48.dp),
                shape = shape,
                color = if (isSelected) Color(0xFFE62129) else Color.White,
                border = BorderStroke(1.dp, if (isSelected) Color(0xFFE62129) else Color.LightGray),
                onClick = { onLevelSelected(level) }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = level,
                        color = if (isSelected) Color.White else Color.Black,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}