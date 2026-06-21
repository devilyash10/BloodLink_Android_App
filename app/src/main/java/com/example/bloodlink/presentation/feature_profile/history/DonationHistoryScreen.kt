package com.example.bloodlink.presentation.feature_profile.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.common.CustomTopAppBar

@Composable
fun DonationHistoryScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFFFAFAFA))
    ) {
        CustomTopAppBar(
            title = "Donation History",
            onBackClick = onNavigateBack
        )

        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Next Eligible Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFE62129))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Next Eligible Donation", color = Color(0xFFE62129), fontSize = 12.sp)
                            Text("20 Apr 2024", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                            Text("You can donate again", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }

            // History Timeline
            item {
                TimelineItem(date = "12 Jan 2024", location = "Apollo Hospital, Bangalore", isFirst = true)
            }
            item {
                TimelineItem(date = "10 Sep 2023", location = "Red Cross Blood Bank", isFirst = false)
            }
            item {
                TimelineItem(date = "15 May 2023", location = "Jeevan Blood Bank", isFirst = false, isLast = true)
            }
        }
    }
}

@Composable
fun TimelineItem(date: String, location: String, isFirst: Boolean = false, isLast: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        // Timeline Dot and Line
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight()
                .drawBehind {
                    if (!isLast) {
                        drawLine(
                            color = Color(0xFFEEEEEE),
                            start = Offset(size.width / 2, if (isFirst) 20.dp.toPx() else 0f),
                            end = Offset(size.width / 2, size.height),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(12.dp)
                    .background(if (isFirst) Color(0xFFE62129) else Color.LightGray, CircleShape)
            )
        }

        // Content
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = date, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = location, color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DonationHistoryScreenPreview() {
    DonationHistoryScreen(onNavigateBack = {})
}