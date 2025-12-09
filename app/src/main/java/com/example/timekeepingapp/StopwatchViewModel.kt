package com.example.timekeepingapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class StopwatchViewModel(): ViewModel() {

    private val _repository: StopwatchRepository = StopwatchRepository()

    private val _time = mutableStateOf(_repository.getStopwatch().time)
    private val _isRunning = mutableStateOf(_repository.getStopwatch().isRunning)

    // Expose _time & _isRunning as immutable states
    val time: MutableState<Long> = _time
    val isRunning: MutableState<Boolean> = _isRunning
}