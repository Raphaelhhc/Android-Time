package com.example.timeapp.ui.stopwatch

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StopwatchScreen(
    vm: StopwatchViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Stopwatch",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Current elapsed time
        Text(
            text = formatTime(vm.elapsedTime.longValue),
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Lap button - become reset button when stopping
            Button(
                onClick = {
                    if (vm.stopwatchState.value == StopwatchState.STOPPING)
                        vm.onClickReset()
                    else
                        vm.onClickLap()
                }
            ) {
                if (vm.stopwatchState.value == StopwatchState.STOPPING)
                    Text("Reset")
                else
                    Text("Lap")
            }

            // Start button - become Stop button when counting
            Button(
                onClick = {
                    if (vm.stopwatchState.value == StopwatchState.RUNNING)
                        vm.onClickStop()
                    else
                        vm.onClickStart()
                }
            ) {
                if (vm.stopwatchState.value == StopwatchState.RUNNING)
                    Text("Stop")
                else
                    Text("Start")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of lap time records
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            vm.lapTimeRecords.forEach { value ->
                Text(text = formatTime(value))
            }
        }
    }
}

// Format timestamp to 00:00.00
@SuppressLint("DefaultLocale")
fun formatTime(elapsedTime: Long): String {
    val totalSeconds = elapsedTime / 1_000.0
    val minutes = (totalSeconds / 60.0).toInt()
    val seconds = totalSeconds % 60
    return String.format("%02d:%05.2f", minutes, seconds)
}
