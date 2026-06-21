package com.example.bloodlink.presentation.feature_auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.buttons.PrimaryRedButton
import com.example.bloodlink.presentation.components.inputs.StandardTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // State variables to hold user input
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    // State for the Dropdown menu
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()) // Allows scrolling if screen is small
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Back Button
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.offset(x = (-12).dp) // Adjusts alignment to match the text edge
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Navigate Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Headers
        Text(
            text = "Create Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Let's get you started",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Full Name Field
        StandardTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Full Name",
            leadingIcon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number Field
        StandardTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = "Phone Number",
            leadingIcon = Icons.Default.Phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Blood Group Dropdown Menu
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            StandardTextField(
                value = bloodGroup,
                onValueChange = {}, // Read-only, so we don't need to capture text input here
                label = "Blood Group",
                leadingIcon = Icons.Default.WaterDrop,
                readOnly = true, // Prevents keyboard from opening
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                modifier = Modifier.menuAnchor() // Critical: Binds the menu to this text field
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

        // City Field
        StandardTextField(
            value = city,
            onValueChange = { city = it },
            label = "City",
            leadingIcon = Icons.Default.LocationCity
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Sign Up Button
        PrimaryRedButton(
            text = "Sign Up",
            onClick = { /* TODO: Trigger Sign Up Logic in ViewModel */ },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Footer Text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Already have an account? ", color = Color.Gray)
            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = "Login",
                    color = Color(0xFFE62129),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp)) // Safe area padding at the bottom
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        onNavigateToLogin = {},
        onNavigateBack = {}
    )
}
