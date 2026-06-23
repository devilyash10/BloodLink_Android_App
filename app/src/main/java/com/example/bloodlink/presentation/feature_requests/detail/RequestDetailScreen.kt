package com.example.bloodlink.presentation.feature_requests.detail

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.bloodlink.domain.model.User
import com.example.bloodlink.domain.util.BloodCompatibility
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun RequestDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: RequestDetailViewModel = hiltViewModel()
) {
    val request by viewModel.request.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isOwner by viewModel.isOwner.collectAsState()
    val isHospital by viewModel.isHospital.collectAsState()
    val isCompleted by viewModel.isCompleted.collectAsState()
    val respondingHeroes by viewModel.respondingHeroes.collectAsState()

    val requesterName by viewModel.requesterName.collectAsState()
    val requesterPhone by viewModel.requesterPhone.collectAsState()
    val requesterType by viewModel.requesterType.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val context = LocalContext.current
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Resolution Dialog tracking states
    var showResolutionDialog by remember { mutableStateOf(false) }
    var currentResolutionStep by remember { mutableStateOf(1) } // 1: Source Selection, 2: Hero Picker

    val hasResponded = respondingHeroes.any { it.id == currentUser?.id }

    Scaffold(
        bottomBar = {
            request?.let { req ->
                if (!isCompleted) {
                    Surface(color = Color.White, shadowElevation = 16.dp, modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            if (isOwner) {
                                // OWNER VIEW: Access to Resolution flow
                                Button(
                                    onClick = {
                                        currentResolutionStep = 1
                                        showResolutionDialog = true
                                    },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62129)),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Mark as Completed", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            } else if (isHospital) {
                                // HOSPITAL VIEW: Access to Vault fulfillment
                                Button(
                                    onClick = { viewModel.resolveFromOtherSources() },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(Icons.Default.LocalHospital, contentDescription = null)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Fulfill from Hospital Vault", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                // GENERAL DONOR VIEW
                                val donorGroup = currentUser?.bloodGroup ?: ""
                                val safeReqGroup = req.bloodGroup.ifBlank { "Unknown" }
                                val isCompatible = donorGroup.isNotBlank() && BloodCompatibility.canDonate(donorGroup, safeReqGroup)

                                if (hasResponded) {
                                    Button(
                                        onClick = { },
                                        enabled = false,
                                        modifier = Modifier.fillMaxWidth().height(56.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            disabledContainerColor = Color(0xFFE8F5E9),
                                            disabledContentColor = Color(0xFF388E3C)
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text("You Have Responded", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else if (isCompatible || safeReqGroup == "Unknown") {
                                    Button(
                                        onClick = { showConfirmDialog = true },
                                        modifier = Modifier.fillMaxWidth().height(56.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text("I Can Donate", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                                        border = BorderStroke(1.dp, Color(0xFFE62129).copy(alpha = 0.5f))
                                    ) {
                                        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFD32F2F))
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text("Medical Mismatch", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                                                Text("Your blood group ($donorGroup) cannot be donated to a patient requiring $safeReqGroup.", color = Color.DarkGray, fontSize = 13.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(paddingValues)
        ) {
            CustomTopAppBar(title = "Request Details", onBackClick = onNavigateBack)

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFFE62129))
            }

            request?.let { req ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    if (isCompleted) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                            ) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF388E3C))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("This request has been successfully fulfilled.", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(if (isCompleted) Color.Gray else Color(0xFFE62129), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = req.bloodGroup.ifBlank { "O+" },
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 28.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = req.patientName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.background(Color.Gray, RoundedCornerShape(4.dp)).padding(2.dp)) {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = req.hospitalName, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = req.locationArea, color = Color.Gray, fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp)).padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "${req.unitsRequired}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE62129))
                                Text(text = "Units", fontSize = 12.sp, color = Color.Gray)
                            }
                            Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color(0xFFEEEEEE)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val uColor = if (req.urgencyLevel == "CRITICAL") Color(0xFFD32F2F) else Color(0xFFF57C00)
                                Text(text = req.urgencyLevel, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = uColor)
                                Text(text = "Urgency", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }

                    if (req.additionalNotes.isNotBlank()) {
                        item {
                            Text("Additional Notes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = req.additionalNotes, color = Color.DarkGray, fontSize = 14.sp, lineHeight = 20.sp)
                        }
                    }

                    item {
                        Text("Requested By", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier.size(48.dp).background(Color(0xFFF5F5F5), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (requesterType == "HOSPITAL") Icons.Default.LocalHospital else Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Color(0xFFE62129)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(text = requesterName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                                        Text(text = if (requesterType == "HOSPITAL") "Hospital / Blood Bank" else "Individual Requester", color = Color.Gray, fontSize = 13.sp)
                                    }
                                }

                                if (!isOwner && !isCompleted) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    HorizontalDivider(color = Color(0xFFF5F5F5))
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        OutlinedButton(
                                            onClick = {
                                                if (requesterPhone.isNotBlank()) {
                                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$requesterPhone"))
                                                    context.startActivity(intent)
                                                }
                                            },
                                            modifier = Modifier.weight(1f).height(40.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Call")
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                if (requesterPhone.isNotBlank()) {
                                                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$requesterPhone"))
                                                    context.startActivity(intent)
                                                }
                                            },
                                            modifier = Modifier.weight(1f).height(40.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Message")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Responding Heroes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Heroes who have offered to help with this request.", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))

                        if (respondingHeroes.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.White, RoundedCornerShape(12.dp)).border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                Text("Waiting for heroes to respond...", color = Color.Gray, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    items(respondingHeroes) { hero ->
                        HeroRespondentCard(hero = hero)
                    }
                }
            }
        }
    }

    // --- DONOR STANDALONE CONFIRMATION DIALOG ---
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = Color.White,
            title = { Text("Confirm Donation", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to commit to donating blood for ${request?.patientName}?", color = Color.DarkGray) },
            confirmButton = {
                Button(onClick = { viewModel.respondToRequest(); showConfirmDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))) {
                    Text("Yes, I Will Donate")
                }
            },
            dismissButton = { TextButton(onClick = { showConfirmDialog = false }) { Text("Cancel", color = Color.Gray) } }
        )
    }

    // --- TWO-STAGE OWNER FULFILLMENT RESOLUTION DIALOG ---
    if (showResolutionDialog) {
        AlertDialog(
            onDismissRequest = { showResolutionDialog = false },
            containerColor = Color.White,
            title = {
                Text(
                    text = if (currentResolutionStep == 1) "Complete Requirement" else "Select App Donor",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    if (currentResolutionStep == 1) {
                        Text("How was this blood requirement fulfilled?", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            onClick = { viewModel.resolveFromOtherSources(); showResolutionDialog = false },
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalHospital, contentDescription = null, tint = Color.Gray)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Arranged from other sources", fontWeight = FontWeight.Medium, color = Color.DarkGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            onClick = {
                                if (respondingHeroes.isNotEmpty()) {
                                    currentResolutionStep = 2
                                } else {
                                    Toast.makeText(context, "No app heroes have responded yet.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            colors = CardDefaults.cardColors(containerColor = if (respondingHeroes.isNotEmpty()) Color(0xFFE8F5E9) else Color(0xFFFAFAFA)),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, if (respondingHeroes.isNotEmpty()) Color(0xFF388E3C).copy(alpha = 0.3f) else Color(0xFFEEEEEE))
                        ) {
                            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Favorite, contentDescription = null, tint = if (respondingHeroes.isNotEmpty()) Color(0xFF388E3C) else Color.LightGray)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "A BloodLink Hero Donated",
                                    fontWeight = FontWeight.Bold,
                                    color = if (respondingHeroes.isNotEmpty()) Color(0xFF388E3C) else Color.LightGray
                                )
                            }
                        }
                    } else {
                        Text("Choose the registered hero who completed the donation to award points:", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))

                        Box(modifier = Modifier.height(200.dp)) {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(respondingHeroes) { hero ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                            .clickable {
                                                viewModel.resolveWithHero(hero.id)
                                                showResolutionDialog = false
                                            }
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(modifier = Modifier.size(32.dp).background(Color(0xFFE3F2FD), CircleShape), contentAlignment = Alignment.Center) {
                                            Text(hero.fullName.take(1), color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(hero.fullName, fontWeight = FontWeight.Bold, color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = {
                        if (currentResolutionStep == 2) currentResolutionStep = 1 else showResolutionDialog = false
                    }
                ) {
                    Text(text = if (currentResolutionStep == 2) "Back" else "Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun HeroRespondentCard(hero: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).background(Color(0xFFE3F2FD), CircleShape), contentAlignment = Alignment.Center) {
                Text(hero.fullName.take(1), color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(hero.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(hero.city, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}