package com.example.bloodlink.presentation.feature_inventory

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun InventoryDashboardScreen(
    onNavigateBack: () -> Unit,
    viewModel: InventoryViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val inventory by viewModel.inventoryState.collectAsState()
    val hasUnsavedChanges by viewModel.hasUnsavedChanges.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()

    var showConfirmDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            Toast.makeText(context, "Inventory Successfully Updated!", Toast.LENGTH_SHORT).show()
            viewModel.resetSaveSuccess()
            onNavigateBack() // Return to profile after saving
        }
    }

    // --- CONFIRMATION DIALOG (Prevents Accidental Typos) ---
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF57C00))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirm Vault Update", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    "You are about to overwrite your hospital's live blood inventory. This data will be instantly visible to patients across the BloodLink network.\n\nProceed with update?",
                    fontSize = 14.sp
                )
            },
            containerColor = Color.White,
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.saveInventoryToDatabase()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129))
                ) {
                    Text("Publish to Network")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Review Again", color = Color.Gray) }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            if (hasUnsavedChanges) {
                ExtendedFloatingActionButton(
                    onClick = { showConfirmDialog = true },
                    containerColor = Color(0xFF388E3C), // Green for save
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Save, contentDescription = null) },
                    text = { Text("Save & Publish", fontWeight = FontWeight.Bold) }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFFAFAFA))
        ) {
            CustomTopAppBar(title = "Live Inventory", onBackClick = onNavigateBack)

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFFE62129))
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Update the number of units currently available in your vault. Mark critical groups as 'Out of Stock' immediately.",
                        color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Map over the blood groups
                val groups = listOf("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")
                items(groups) { bg ->
                    val stock = inventory[bg] ?: 0
                    BloodStockControlCard(
                        bloodGroup = bg,
                        currentStock = stock,
                        onIncrement = { viewModel.updateStock(bg, 1) },
                        onDecrement = { viewModel.updateStock(bg, -1) },
                        onMarkZero = { viewModel.markOutOfStock(bg) }
                    )
                }
            }
        }
    }
}

@Composable
fun BloodStockControlCard(
    bloodGroup: String,
    currentStock: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onMarkZero: () -> Unit
) {
    val isOutOfStock = currentStock == 0

    Card(
        modifier = Modifier.fillMaxWidth().border(
            width = 1.dp,
            color = if (isOutOfStock) Color(0xFFFFEBEE) else Color(0xFFEEEEEE),
            shape = RoundedCornerShape(16.dp)
        ),
        colors = CardDefaults.cardColors(containerColor = if (isOutOfStock) Color(0xFFFFF8F8) else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Side: Badge & Name
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(56.dp).background(if (isOutOfStock) Color.LightGray else Color(0xFFFFEBEE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = bloodGroup, color = if (isOutOfStock) Color.DarkGray else Color(0xFFE62129), fontWeight = FontWeight.Black, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = "Units Available", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        text = if (isOutOfStock) "OUT OF STOCK" else "$currentStock Units",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isOutOfStock) Color.Red else Color.Black
                    )
                }
            }

            // Right Side: Controls
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Minus Button
                    IconButton(
                        onClick = onDecrement,
                        modifier = Modifier.size(36.dp).background(Color(0xFFF5F5F5), CircleShape)
                    ) { Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.DarkGray) }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Plus Button
                    IconButton(
                        onClick = onIncrement,
                        modifier = Modifier.size(36.dp).background(Color(0xFFE8F5E9), CircleShape)
                    ) { Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color(0xFF388E3C)) }
                }

                if (!isOutOfStock) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mark Empty",
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onMarkZero() }.padding(end = 4.dp)
                    )
                }
            }
        }
    }
}