package com.example.bloodlink.presentation.feature_auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.presentation.components.buttons.PrimaryRedButton
import com.example.bloodlink.presentation.components.inputs.StandardTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    // --- NEW: Account Type State ---
    var userType by remember { mutableStateOf("INDIVIDUAL") } // "INDIVIDUAL" or "HOSPITAL"

    // --- NEW: Dropdown State for Blood Group ---
    var bloodGroup by remember { mutableStateOf("O+") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val signUpSuccess by viewModel.signUpSuccess.collectAsState()

    LaunchedEffect(signUpSuccess) {
        if (signUpSuccess) onNavigateToHome()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Text(text = "Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE62129))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Join the BloodLink network", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(32.dp))

        // --- ACCOUNT TYPE SELECTOR ---
        Row(
            modifier = Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(12.dp)).border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Individual Tab
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight()
                    .background(if (userType == "INDIVIDUAL") Color(0xFFE62129) else Color.Transparent)
                    .clickable { userType = "INDIVIDUAL" },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Individual", color = if (userType == "INDIVIDUAL") Color.White else Color.Gray, fontWeight = FontWeight.Bold)
            }
            // Hospital Tab
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight()
                    .background(if (userType == "HOSPITAL") Color(0xFF1976D2) else Color.Transparent)
                    .clickable { userType = "HOSPITAL" },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Hospital / Bank", color = if (userType == "HOSPITAL") Color.White else Color.Gray, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Dynamic Name Field
        StandardTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = if (userType == "HOSPITAL") "Hospital / Organization Name" else "Full Name",
            leadingIcon = if (userType == "HOSPITAL") Icons.Default.LocalHospital else Icons.Default.Person
        )
        Spacer(modifier = Modifier.height(16.dp))

        StandardTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            leadingIcon = Icons.Default.Email
        )
        Spacer(modifier = Modifier.height(16.dp))

        StandardTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = "Phone Number",
            leadingIcon = Icons.Default.Phone
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- DYNAMIC BLOOD GROUP FIELD ---
        // We completely hide this if they are signing up as a Hospital!
        if (userType == "INDIVIDUAL") {
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                StandardTextField(
                    value = bloodGroup,
                    onValueChange = {},
                    label = "Your Blood Group",
                    readOnly = true,
                    leadingIcon = Icons.Default.Bloodtype,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    bloodGroups.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                bloodGroup = selectionOption
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        StandardTextField(
            value = city,
            onValueChange = { city = it },
            label = "City",
            leadingIcon = Icons.Default.LocationCity
        )
        Spacer(modifier = Modifier.height(16.dp))

        StandardTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation()
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryRedButton(
            text = if (isLoading) "Creating Account..." else "Sign Up",
            onClick = {
                viewModel.signUp(
                    email,
                    password,
                    fullName,
                    if(userType == "HOSPITAL") "N/A" else bloodGroup,
                    city,
                    phoneNumber,
                    userType // Passing it down!
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.padding(bottom = 32.dp)) {
            Text(text = "Already have an account? ", color = Color.Gray)
            Text(
                text = "Log In",
                color = Color(0xFFE62129),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}