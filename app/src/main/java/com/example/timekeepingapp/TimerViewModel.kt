package com.example.timekeepingapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TimerViewModel(timer: TimerModel = TimerModel(0, 30L, 30L, false)): ViewModel() {

    private val _time = mutableStateOf(timer.time)
    private val _isRunning = mutableStateOf(timer.isRunning)


    // Expose _time & _isRunning as immutable states
    val time: MutableState<Long> = _time
    val isRunning: MutableState<Boolean> = _isRunning
    val id = timer.id
}