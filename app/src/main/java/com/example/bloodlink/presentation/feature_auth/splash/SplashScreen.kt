package com.example.bloodlink.presentation.feature_auth.splash

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Simulate loading delay
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onNavigateNext()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Center Logo
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE62129), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for the exact heartbeat-blood-drop logo
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "BloodLink Logo",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "BloodLink",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE62129)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Donate Blood, Save Lives",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Bottom Red Wave
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
        ) {
            val path = Path().apply {
                moveTo(0f, size.height * 0.5f)
                quadraticBezierTo(
                    size.width * 0.25f, size.height * 0.1f,
                    size.width * 0.5f, size.height * 0.5f
                )
                quadraticBezierTo(
                    size.width * 0.75f, size.height * 0.9f,
                    size.width, size.height * 0.4f
                )
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path = path, color = Color(0xFFE62129))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onNavigateNext = {})
}