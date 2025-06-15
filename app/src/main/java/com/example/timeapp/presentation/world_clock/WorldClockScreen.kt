package com.example.timeapp.presentation.world_clock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.timeapp.presentation.AddCityScreen
import com.example.timeapp.presentation.timer.TimerViewModel

@Composable
fun WorldClockScreen(
    vm: WorldClockViewModel = hiltViewModel(),
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

        // TODO Column to display selected city and time
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            items(vm.selectedCityTime.size) { idx ->
                // TODO Display City name - Local Time - Delete button
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
    vm: WorldClockViewModel = hiltViewModel(),
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

        // TODO Column to display selected city and time
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            items(vm.allCityTime.size) { idx ->
                // TODO Display City name - Local Time - Add button
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