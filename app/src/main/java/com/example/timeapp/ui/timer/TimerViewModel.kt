package com.example.timeapp.ui.timer

import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
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
class TimerViewModel @Inject constructor() : ViewModel() {

    // Time set from UI
    private var setHours = 0
    private var setMinutes = 0
    private var setSeconds = 0

    // Current state of timer
    private val _timerState = mutableStateOf(TimerState.DEFAULT)
    val timerState get() = _timerState

    // Count down time shown on UI
    private val _countDownTime = mutableLongStateOf(0L)
    val countDownTime get() = _countDownTime

    private var initialCountDownTime = 0L

    // Current elapsed time
    private var elapsedTime = 0L

    // Record time during timer running
    private var startTime = 0L
    private var accumulatedTime = 0L

    init {
        viewModelScope.launch(Dispatchers.Default) {
            snapshotFlow { _timerState.value }
                .distinctUntilChanged()
                .collect { state ->
                    if (state == TimerState.RUNNING) {

                        while (_timerState.value == TimerState.RUNNING) {
                            val currentTime = SystemClock.elapsedRealtime()
                            elapsedTime = accumulatedTime + (currentTime - startTime)
                            _countDownTime.longValue = (initialCountDownTime - elapsedTime).coerceAtLeast(0L)
                            delay(REFRESH_INTERVAL_MS)
                        }

                    }
                }
        }
    }

    private fun updateCountDownTime() {
        _countDownTime.longValue =
            (setHours * 3_600 + setMinutes * 60 + setSeconds) * 1_000L
    }

    fun onSetHours(hour: Int) {
        setHours = hour
        updateCountDownTime()
        Log.d("VM", "update time | set hour $setHours | countdown time ${_countDownTime.value}")
    }

    fun onSetMinutes(minute: Int) {
        setMinutes = minute
        updateCountDownTime()
        Log.d("VM", "update time | set minute $setMinutes | countdown time ${_countDownTime.value}")
    }

    fun onSetSeconds(second: Int) {
        setSeconds = second
        updateCountDownTime()
        Log.d("VM", "update time | set second $setSeconds | countdown time ${_countDownTime.value}")
    }

    fun onClickStart() {
        initialCountDownTime = _countDownTime.longValue
        startTime = SystemClock.elapsedRealtime()
        _timerState.value = TimerState.RUNNING
    }

    fun onClickPause() {
        val now = SystemClock.elapsedRealtime()
        elapsedTime = accumulatedTime + (now - startTime)
        accumulatedTime = elapsedTime
        _timerState.value = TimerState.PAUSE
    }

    fun onClickCancel() {
        _timerState.value = TimerState.DEFAULT
        startTime = 0L
        accumulatedTime = 0L
        elapsedTime = 0L
        _countDownTime.longValue = 0L
    }

}