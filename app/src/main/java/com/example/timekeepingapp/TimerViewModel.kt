package com.example.timekeepingapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TimerViewModel(): ViewModel() {

    private val _repository: TimerRepository = TimerRepository()

    private val _time = mutableStateOf(_repository.getTimer().time)
    private val _isRunning = mutableStateOf(_repository.getTimer().isRunning)

    // Expose _time & _isRunning as immutable states
    val time: MutableState<Long> = _time
    val isRunning: MutableState<Boolean> = _isRunning
}