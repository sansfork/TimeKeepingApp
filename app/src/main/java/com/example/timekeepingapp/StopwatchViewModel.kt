package com.example.timekeepingapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class StopwatchViewModel(stopwatch: StopwatchModel = StopwatchModel(0)): ViewModel() {

    private val _time = mutableStateOf(stopwatch.time)
    private val _isRunning = mutableStateOf(stopwatch.isRunning)

    // Expose _time & _isRunning as immutable states
    val time: MutableState<Long> = _time
    val isRunning: MutableState<Boolean> = _isRunning
    val id = stopwatch.id
}