package com.example.timeapp.presentation.timer

import android.os.SystemClock
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeapp.presentation.timer.notification.TimerAlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REFRESH_INTERVAL_MS = 16L

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val alarmScheduler: TimerAlarmScheduler
) : ViewModel() {

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
                            if (_countDownTime.longValue == 0L) {
                                _timerState.value = TimerState.DEFAULT
                            }
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
    }

    fun onSetMinutes(minute: Int) {
        setMinutes = minute
        updateCountDownTime()
    }

    fun onSetSeconds(second: Int) {
        setSeconds = second
        updateCountDownTime()
    }

    fun onClickStart() {
        initialCountDownTime = _countDownTime.longValue
        startTime = SystemClock.elapsedRealtime()
        if (alarmScheduler.canScheduleExactAlarms()) {
            alarmScheduler.schedule(initialCountDownTime)
            _timerState.value = TimerState.RUNNING
        } else {
            alarmScheduler.requestExactAlarmPermission()
        }
    }

    fun onClickPause() {
        val now = SystemClock.elapsedRealtime()
        elapsedTime = accumulatedTime + (now - startTime)
        accumulatedTime = elapsedTime
        alarmScheduler.cancel()
        _timerState.value = TimerState.PAUSE
    }

    fun onClickCancel() {
        alarmScheduler.cancel()
        _timerState.value = TimerState.DEFAULT
        startTime = 0L
        accumulatedTime = 0L
        elapsedTime = 0L
        _countDownTime.longValue = 0L
    }

}