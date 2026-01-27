package com.example.timekeepingapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class TimerListViewModel(application: Application): AndroidViewModel(application) {

    private val _timerList = MutableStateFlow<List<Timer>>(emptyList())
    val timerList = _timerList.asStateFlow()

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    init {
        viewModelScope.launch {
            context.timerFlow().collect { timers ->
                _timerList.value = timers
            }
        }
    }

    fun get(itemIndex: Int): Timer {
        return timerList.value[itemIndex]
    }

    fun AddTimer(newTimer: Timer) {
        viewModelScope.launch {
            val updatedList = _timerList.value + newTimer
            context.saveTimers(updatedList)
        }
    }

    fun RemoveTimerById(itemId: Int) {
        viewModelScope.launch {
            val updatedList = _timerList.value.filterNot { it.id == itemId }
            context.saveTimers(updatedList)
        }
    }

    fun timeLoop() {
        val updatedList = _timerList.value.map { timer ->
            if (timer.isRunning && timer.time > 0) {
                timer.copy(time = timer.time - 1)
            } else {
                timer
            }
        }
        // We don't save the updated list to DataStore here to avoid excessive writes.
        // The user can save the state manually if needed.
        _timerList.value = updatedList
    }
}