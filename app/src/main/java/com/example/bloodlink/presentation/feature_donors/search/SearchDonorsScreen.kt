package com.example.bloodlink.presentation.feature_donors.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bloodlink.presentation.components.buttons.PrimaryRedButton
import com.example.bloodlink.presentation.components.common.CustomTopAppBar
import com.example.bloodlink.presentation.components.inputs.BloodGroupSelector
import com.example.bloodlink.presentation.components.inputs.SearchBar

@Composable
fun SearchDonorsScreen(
    onNavigateBack: () -> Unit,
    // UPDATED: Now passes the filters out to the NavHost!
    onSearchClicked: (bloodGroup: String, query: String, distance: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("All") }
    var useMyLocation by remember { mutableStateOf(true) }
    var distance by remember { mutableStateOf(5f) }

    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 24.dp)
    ) {
        CustomTopAppBar(
            title = "Search Donors",
            onBackClick = onNavigateBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Blood Group", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        BloodGroupSelector(
            selectedGroup = selectedBloodGroup,
            onGroupSelected = { selectedBloodGroup = it },
            includeAllOption = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Nearby", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Use my location", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = useMyLocation,
                    onCheckedChange = { useMyLocation = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFE62129))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Distance", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = distance,
            onValueChange = { distance = it },
            valueRange = 1f..50f,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFE62129),
                activeTrackColor = Color(0xFFE62129)
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${distance.toInt()} KM", color = Color(0xFFE62129), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("50 KM", color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))

        PrimaryRedButton(
            text = "Search Donors",
            // UPDATED: Fires the callback with the current state values
            onClick = { onSearchClicked(selectedBloodGroup, searchQuery, distance) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { /* Open Bottom Sheet */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Advanced Filters", color = Color.Gray)
        }
    }
}