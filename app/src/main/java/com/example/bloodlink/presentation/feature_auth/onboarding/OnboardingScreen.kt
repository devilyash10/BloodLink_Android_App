package com.example.bloodlink.presentation.feature_auth.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.buttons.PrimaryRedButton

@Composable
fun OnboardingScreen(
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Title with highlighted text
        Text(
            text = buildAnnotatedString {
                append("Together, we\ncan ")
                withStyle(style = SpanStyle(color = Color(0xFFE62129))) {
                    append("save lives")
                }
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Donate blood and help those\nin need in your community.",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Illustration Placeholder
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(Color(0xFFFFEBEE), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Illustration Placeholder", color = Color(0xFFE62129))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Pagination Dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE62129)))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.LightGray))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.LightGray))
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryRedButton(
            text = "Next",
            onClick = onNavigateNext,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(onNavigateNext = {})
}