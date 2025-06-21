package com.example.timeapp.presentation.alarm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.snapshotFlow
import com.example.timeapp.presentation.timer.HOURS
import com.example.timeapp.presentation.timer.MINUTES
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val ITEM_HEIGHT = 50.dp
private const val VISIBLE_COUNT = 3
private val CENTER_OFFSET = VISIBLE_COUNT / 2

@Composable
fun AlarmScreen(
    vm: AlarmViewModel
) {
    var isAddingAlarm by remember { mutableStateOf(false) }
    var isEditingAlarm by remember { mutableStateOf(false) }
    var editingAlarmId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            "Alarm",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // List of alarms
        // Each row of alarm: alarm time, edit button, delete button
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(vm.alarms.size) { idx ->
                val alarm = vm.alarms[idx]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(formatLocalTime(alarm.alarmTime))

                        Button(
                            onClick = {
                                if (alarm.activated) {
                                    vm.inactivateAlarm(alarm.id)
                                } else {
                                    vm.activateAlarm(alarm.id)
                                }
                            },
                        ) {
                            if (alarm.activated) {
                                Text("Turn Off")
                            } else {
                                Text("Turn On")
                            }
                        }

                        Button(
                            onClick = {
                                isAddingAlarm = false
                                isEditingAlarm = true
                                editingAlarmId = alarm.id
                            }
                        ) {
                            Text("Edit")
                        }

                        Button(
                            onClick = {
                                vm.deleteAlarm(alarm.id)
                            }
                        ) {
                            Text("Delete")
                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add new alarm button
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                isEditingAlarm = false
                isAddingAlarm = true
            }
        ) {
            Text("Add New Alarm")
        }

        // Drawers
        if (isAddingAlarm) {
            AddAlarmDrawer(
                onDismiss = { isAddingAlarm = false },
                onCreate = {
                    vm.addNewAlarm(it, true)
                    isAddingAlarm = false
                }
            )
        }

        if (isEditingAlarm) {
            val alarm = vm.alarms.firstOrNull { it.id == editingAlarmId }
            if (alarm != null) {
                EditAlarmDrawer(
                    alarm = alarm,
                    onDismiss = {
                        isEditingAlarm = false
                        editingAlarmId = null
                    },
                    onConfirm = { time ->
                        vm.editAlarm(alarm.id, time)
                        if (alarm.activated) {
                            vm.activateAlarm(alarm.id)
                        }
                        isEditingAlarm = false
                        editingAlarmId = null
                    }
                )
            }
        }

    }
}

@Composable
fun AddAlarmDrawer(
    onDismiss: () -> Unit,
    onCreate: (LocalTime) -> Unit
) {
    val hourState = rememberLazyListState()
    val minuteState = rememberLazyListState()

    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }

    LaunchedEffect(hourState) {
        snapshotFlow { hourState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { idx ->
                selectedHour = HOURS.getOrElse(idx) { HOURS.last() }
            }
    }
    LaunchedEffect(minuteState) {
        snapshotFlow { minuteState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { idx ->
                selectedMinute = MINUTES.getOrElse(idx) { MINUTES.last() }
            }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Add Alarm", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimePickerColumn(state = hourState, items = HOURS)
                Text(":", fontSize = 24.sp)
                TimePickerColumn(state = minuteState, items = MINUTES)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onCreate(LocalTime.of(selectedHour, selectedMinute))
                }
            ) { Text("Create") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                onClick = onDismiss
            ) { Text("Close") }
        }
    }
}

@Composable
fun EditAlarmDrawer(
    alarm: Alarm,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    val hourState = rememberLazyListState(initialFirstVisibleItemIndex = alarm.alarmTime.hour)
    val minuteState = rememberLazyListState(initialFirstVisibleItemIndex = alarm.alarmTime.minute)

    var selectedHour by remember { mutableIntStateOf(alarm.alarmTime.hour) }
    var selectedMinute by remember { mutableIntStateOf(alarm.alarmTime.minute) }

    LaunchedEffect(hourState) {
        snapshotFlow { hourState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { idx ->
                selectedHour = HOURS.getOrElse(idx) { HOURS.last() }
            }
    }
    LaunchedEffect(minuteState) {
        snapshotFlow { minuteState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { idx ->
                selectedMinute = MINUTES.getOrElse(idx) { MINUTES.last() }
            }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Edit Alarm", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimePickerColumn(state = hourState, items = HOURS)
                Text(":", fontSize = 24.sp)
                TimePickerColumn(state = minuteState, items = MINUTES)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onConfirm(LocalTime.of(selectedHour, selectedMinute))
                }
            ) { Text("Confirm") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                onClick = onDismiss
            ) { Text("Close") }
        }
    }
}

@Composable
private fun TimePickerColumn(state: LazyListState, items: List<Int>) {
    LazyColumn(
        state = state,
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(vertical = ITEM_HEIGHT * CENTER_OFFSET),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items.size) { i ->
            Box(
                modifier = Modifier
                    .height(ITEM_HEIGHT)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(String.format("%02d", items[i]), fontSize = 20.sp)
            }
        }
    }
}

private fun formatLocalTime(time: LocalTime): String {
    return time.format(
        DateTimeFormatter.ofPattern("HH:mm")
    )
}