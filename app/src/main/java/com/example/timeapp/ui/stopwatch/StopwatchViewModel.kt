package com.example.timeapp.ui.stopwatch

import android.os.SystemClock
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REFRESH_INTERVAL_MS = 16L

@HiltViewModel
class StopwatchViewModel @Inject constructor() : ViewModel() {

    // Current elapsed time
    private val _elapsedTime = mutableLongStateOf(0L)
    val elapsedTime get() = _elapsedTime

    // Current recorded lap time
    private val _lapTimeRecords = mutableStateListOf<Long>()
    val lapTimeRecords get() = _lapTimeRecords

    // Current state of stopwatch
    private val _stopwatchState = mutableStateOf(StopwatchState.DEFAULT)
    val stopwatchState get() = _stopwatchState

    // Record time during stopwatch running
    private var startTime = 0L
    private var accumulatedTime = 0L

    init {
        viewModelScope.launch(Dispatchers.Default) {
            snapshotFlow { _stopwatchState.value }
                .distinctUntilChanged()
                .collect { state ->
                    if (state == StopwatchState.RUNNING) {

                        while (_stopwatchState.value == StopwatchState.RUNNING) {
                            val currentTime = SystemClock.elapsedRealtime()
                            _elapsedTime.longValue = accumulatedTime + (currentTime - startTime)
                            delay(REFRESH_INTERVAL_MS)
                        }

                    }
                }
        }
    }

    // Button click of "Start"
    fun onClickStart() {
        startTime = SystemClock.elapsedRealtime()
        _stopwatchState.value = StopwatchState.RUNNING
    }

    // Button click of "Stop"
    fun onClickStop() {
        _stopwatchState.value = StopwatchState.STOPPING
        accumulatedTime = _elapsedTime.longValue
    }

    // Button click of "Lap"
    fun onClickLap() {
        if (_stopwatchState.value == StopwatchState.RUNNING) {
            val currentElapsedTime = _elapsedTime.longValue
            _lapTimeRecords.add(currentElapsedTime)
        }
    }

    // Button click of "Reset"
    fun onClickReset() {
        _stopwatchState.value = StopwatchState.DEFAULT
        startTime = 0L
        accumulatedTime = 0L
        _elapsedTime.longValue = 0L
        _lapTimeRecords.clear()
    }

}