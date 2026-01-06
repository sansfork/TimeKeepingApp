package com.example.timekeepingapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class IntervalViewModel(interval: IntervalModel = IntervalModel(0)): ViewModel() {

    private val _workTime = mutableStateOf(interval.workTime)
    private val _breakTime = mutableStateOf(interval.breakTime)
    private val _sets = mutableStateOf(interval.sets)
    private val _setsDone = mutableStateOf(interval.setsDone)
    private val _isWorking = mutableStateOf(interval.isWorking)
    private val _isRunning = mutableStateOf(interval.isRunning)

    // Expose _workTime, _breakTime, _sets & _isRunning as immutable states
    val workTime: MutableState<Long> = _workTime
    val breakTime: MutableState<Long> = _breakTime
    val sets: MutableState<Int> = _sets
    val setsDone: MutableState<Int> = _setsDone
    val isWorking: MutableState<Boolean> = _isWorking
    val isRunning: MutableState<Boolean> = _isRunning

}
