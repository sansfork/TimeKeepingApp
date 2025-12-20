package com.example.timekeepingapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class IntervalViewModel(): ViewModel() {

    private val _repository: IntervalRepository = IntervalRepository()

    private val _workTime = mutableStateOf(_repository.getTimer().workTime)
    private val _breakTime = mutableStateOf(_repository.getTimer().breakTime)
    private val _sets = mutableStateOf(_repository.getTimer().sets)
    private val _setsDone = mutableStateOf(_repository.getTimer().setsDone)
    private val _isWorking = mutableStateOf(_repository.getTimer().isWorking)
    private val _isRunning = mutableStateOf(_repository.getTimer().isRunning)

    // Expose _workTime, _breakTime, _sets & _isRunning as immutable states
    val workTime: MutableState<Long> = _workTime
    val breakTime: MutableState<Long> = _breakTime
    val sets: MutableState<Int> = _sets
    val setsDone: MutableState<Int> = _setsDone
    val isWorking: MutableState<Boolean> = _isWorking
    val isRunning: MutableState<Boolean> = _isRunning

}
