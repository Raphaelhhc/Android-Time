package com.example.timeapp.presentation.alarm

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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AlarmScreen(
    vm: AlarmViewModel
) {
    var isAddingAlarm by remember { mutableStateOf(false) }
    var isEditingAlarm by remember { mutableStateOf(false) }

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

    }
}

@Composable
fun AddAlarmDrawer() {
    // Title
    // Dropdown menu for select hour and minute of alarm time
    // Create button
    // Close button
}

@Composable
fun EditAlarmDrawer() {
    // Title
    // Dropdown menu for select hour and minute of alarm time
    // Confirm button
    // Close button
}

private fun formatLocalTime(time: LocalTime): String {
    return time.format(
        DateTimeFormatter.ofPattern("HH:mm")
    )
}