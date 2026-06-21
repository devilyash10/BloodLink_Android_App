package com.example.bloodlink.presentation.components.buttons // FIXED: Matches your actual package path

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.R // FIXED: Points to your correct project namespace R file
import com.example.bloodlink.ui.theme.BloodLinkTheme

@Composable
fun SocialAuthButton(
    text: String,
    iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(percent = 50),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "$text Icon",
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SocialAuthButton(
        text = "Google",
        iconResId = R.drawable.ic_google,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun AppleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SocialAuthButton(
        text = "Apple",
        iconResId = R.drawable.ic_apple,
        onClick = onClick,
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun SocialAuthButtonPreview() {
//    BloodLinkTheme {
//        SocialAuthButton(
//            text = "Google",
//            iconResId = R.drawable.ic_google,
//            onClick = {}
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GoogleSignInButtonPreview() {
//    BloodLinkTheme {
//        GoogleSignInButton(onClick = {})
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AppleSignInButtonPreview() {
//    BloodLinkTheme {
//        AppleSignInButton(onClick = {})
//    }
//}