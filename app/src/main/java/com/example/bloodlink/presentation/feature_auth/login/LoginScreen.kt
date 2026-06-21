package com.example.bloodlink.presentation.feature_auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.core.navigation.Routes
import com.example.bloodlink.presentation.components.buttons.AppleSignInButton
import com.example.bloodlink.presentation.components.buttons.GoogleSignInButton
import com.example.bloodlink.presentation.components.buttons.PrimaryRedButton
import com.example.bloodlink.presentation.components.inputs.StandardTextField

@Composable
fun LoginScreen(
    //viewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Setup State to hold the text the user types
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()      // Take up the whole screen
            .padding(24.dp),    // Add standard 24dp margins on the sides
        verticalArrangement = Arrangement.Center // Center everything vertically
    ) {

        // 2. Added a proper Header styling
        Text(
            text = "Welcome Back",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sign in to continue",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 3. Bound the state to the inputs and added KeyboardOptions
        StandardTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            leadingIcon = Icons.Default.Phone,
            label = "Phone Number",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        StandardTextField(
            value = password,
            onValueChange = { password = it },
            leadingIcon = Icons.Default.Lock,
            label = "Password",
            visualTransformation = PasswordVisualTransformation(), // Hides the password with dots
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Forgot Password?",
                color = Color(0xFFE62129),
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryRedButton(
            text = "Login",
            onClick = {onNavigate("home")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 4. Added horizontal lines to make the "or continue with" text look professional
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            Text(
                text = " or continue with ",
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Added spacedBy(16.dp) to separate the buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GoogleSignInButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            )

            AppleSignInButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // This pushes the "Sign Up" text to the very bottom

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an account?", color = Color.Gray)
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Sign Up", color = Color(0xFFE62129), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onNavigate = {})
}