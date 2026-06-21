package com.bloodlink.app.presentation.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SecondaryOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    val primaryRed = Color(0xFFE62129)

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(percent = 50),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (isEnabled) primaryRed else Color.LightGray
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isEnabled) primaryRed else Color.Gray
        )
    }
}