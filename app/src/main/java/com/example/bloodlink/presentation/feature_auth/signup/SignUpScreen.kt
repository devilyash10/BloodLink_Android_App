package com.example.bloodlink.presentation.feature_auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SignUpScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val signUpSuccess by viewModel.signUpSuccess.collectAsState()

    LaunchedEffect(signUpSuccess) {
        if (signUpSuccess) onNavigateToHome()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp).statusBarsPadding().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.Black)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Full Name") }, shape = RoundedCornerShape(12.dp), singleLine = true)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Email Address") }, shape = RoundedCornerShape(12.dp), singleLine = true)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(12.dp), singleLine = true)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Phone Number") }, shape = RoundedCornerShape(12.dp), singleLine = true)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = bloodGroup, onValueChange = { bloodGroup = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Blood Group (e.g. O+)") }, shape = RoundedCornerShape(12.dp), singleLine = true)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = city, onValueChange = { city = it }, modifier = Modifier.fillMaxWidth(), label = { Text("City") }, shape = RoundedCornerShape(12.dp), singleLine = true)

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color(0xFFE62129), fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFFE62129))
        } else {
            Button(
                onClick = { viewModel.signUp(email, password, name, bloodGroup, city, phone) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Register Now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Text("Already registered? ", color = Color.Gray)
            Text("Login", color = Color(0xFFE62129), fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNavigateToLogin() })
        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun SignUpScreenPreview() {
//    SignUpScreen(
//        onNavigateToLogin = {},
//        onNavigateBack = {},
//        onNavigateToHome = TODO(),
//        viewModel = TODO()
//    )
//}
