package com.example.timeapp.ui.stopwatch

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
            fontSize = 20.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Current elapsed time
        Text(
            text = formatTime(vm.elapsedTime.longValue),
            fontSize = 30.sp,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(vm.lapTimeRecords) {value ->
                Text(text = formatTime(value))
            }
        }
    }
}

// Format timestamp to mm:ss.ss
@SuppressLint("DefaultLocale")
fun formatTime(elapsedTime: Long): String {
    val minutes = (elapsedTime / 60_000).toInt()
    val seconds = ((elapsedTime / 1_000) % 60).toInt()
    val centiSeconds = ((elapsedTime % 1_000) / 10).toInt()
    return String.format("%02d:%02d.%02d", minutes, seconds, centiSeconds)
}
