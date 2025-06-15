package com.example.timeapp.presentation.timer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

private val ITEM_HEIGHT = 50.dp
private const val VISIBLE_COUNT = 3
private val CENTER_OFFSET = VISIBLE_COUNT / 2

@Composable
fun TimerScreen(
    vm: TimerViewModel = hiltViewModel()
) {
    // three list states
    val hourState = rememberLazyListState()
    val minuteState = rememberLazyListState()
    val secondState = rememberLazyListState()

    // whenever the center item index changes, notify the VM
    LaunchedEffect(hourState) {
        snapshotFlow { hourState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { idx ->
                val selected = HOURS.getOrNull(idx)?: HOURS.last()
                vm.onSetHours(selected)
            }
    }
    LaunchedEffect(minuteState) {
        snapshotFlow { minuteState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { idx ->
                val selected = MINUTES.getOrNull(idx) ?: MINUTES.last()
                vm.onSetMinutes(selected)
            }
    }
    LaunchedEffect(secondState) {
        snapshotFlow { secondState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { idx ->
                val selected = SECONDS.getOrNull(idx) ?: SECONDS.last()
                vm.onSetSeconds(selected)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Timer",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            // If in DEFAULT state: scrollable selector for time to alarm
            if (vm.timerState.value == TimerState.DEFAULT) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Scrollable selector of hour
                    LazyColumn(
                        state = hourState,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = ITEM_HEIGHT * CENTER_OFFSET),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(HOURS.size) { i ->
                            Box(
                                modifier = Modifier
                                    .height(ITEM_HEIGHT)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${HOURS[i]}", fontSize = 20.sp)
                            }
                        }
                    }
                    // Scrollable selector of minute
                    LazyColumn(
                        state = minuteState,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = ITEM_HEIGHT * CENTER_OFFSET),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(MINUTES.size) { i ->
                            Box(
                                modifier = Modifier
                                    .height(ITEM_HEIGHT)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${MINUTES[i]}", fontSize = 20.sp)
                            }
                        }
                    }
                    // Scrollable selector of second
                    LazyColumn(
                        state = secondState,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = ITEM_HEIGHT * CENTER_OFFSET),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(SECONDS.size) { i ->
                            Box(
                                modifier = Modifier
                                    .height(ITEM_HEIGHT)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${SECONDS[i]}", fontSize = 20.sp)
                            }
                        }
                    }
                }
            } else {
                // If in RUNNING and PAUSE state: Count down time
                Text(
                    text = formatTime(vm.countDownTime.longValue),
                    fontSize = 40.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel button - activate only in RUNNING or PAUSE state
            Button(
                onClick = { vm.onClickCancel() },
                enabled = vm.timerState.value != TimerState.DEFAULT
            ) {
                Text("Cancel")
            }
            // Start button - become Pause when in RUNNING state
            Button(
                onClick = {
                    if (vm.timerState.value == TimerState.RUNNING)
                        vm.onClickPause()
                    else
                        vm.onClickStart()
                }
            ) {
                if (vm.timerState.value == TimerState.RUNNING)
                    Text("Pause")
                else
                    Text("Start")
            }
        }
    }
}

// Format timestamp to hh:mm:ss
@SuppressLint("DefaultLocale")
fun formatTime(countDownTime: Long): String {
    val totalSeconds = countDownTime / 1_000
    val hours = totalSeconds / 3_600
    val minutes = (totalSeconds % 3_600) / 60
    val seconds = totalSeconds % 60
    val res = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    return res
}