package com.example.bloodlink.presentation.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPinBottomSheet(
    title: String,
    subtitle: String,
    distance: String,
    onActionClick: () -> Unit,
    actionText: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = Color.White
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$distance away", color = Color(0xFFE62129), fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onActionClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129))
            ) {
                Text(actionText)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}