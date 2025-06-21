package com.example.timeapp.presentation.world_clock

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.timeapp.presentation.AddCityScreen
import java.time.LocalTime

@Composable
fun WorldClockScreen(
    vm: WorldClockViewModel,
    nv: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "World Clock",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Column to display selected cities and their time
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            items(vm.selectedCityTime.size) { idx ->
                val zoneId = vm.selectedCityTime[idx].first
                val time = vm.selectedCityTime[idx].second
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${vm.getCityName(zoneId)} - ${formatLocalTime(time)}")
                    Button(onClick = { vm.deleteCityTime(zoneId) }) {
                        Text("Delete")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to move to add city screen
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                nv.navigate(AddCityScreen)
            }
        ) {
            Text("Add City")
        }
    }
}

@Composable
fun AddCityScreen(
    vm: WorldClockViewModel,
    nv: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Add City",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Column to display all available cities that are not selected
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            items(vm.allCityTime.size) { idx ->
                val zoneId = vm.allCityTime[idx].first
                val time = vm.allCityTime[idx].second
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${vm.getCityName(zoneId)} - ${formatLocalTime(time)}")
                    Button(onClick = { vm.addCityTime(zoneId) }) {
                        Text("Add")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to nav back
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                nv.popBackStack()
            }
        ) {
            Text("Back")
        }
    }
}

private fun formatLocalTime(time: LocalTime): String {
    return time.format(
        java.time.format.DateTimeFormatter.ofPattern("HH:mm")
    )
}
