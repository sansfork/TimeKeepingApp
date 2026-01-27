package com.example.timekeepingapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class StopwatchListViewModel(application: Application): AndroidViewModel(application) {

    private val _stopwatchList = MutableStateFlow<List<Stopwatch>>(emptyList())

    val stopwatchList = _stopwatchList.asStateFlow()

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    init {
        viewModelScope.launch {
            context.stopwatchFlow().collect { stopwatches ->
                _stopwatchList.value = stopwatches
            }
        }
    }

    fun get(itemIndex: Int): Stopwatch {
        return stopwatchList.value[itemIndex]
    }

    fun AddStopwatch(newTimer: Stopwatch) {
        viewModelScope.launch {
            val updatedList = _stopwatchList.value + newTimer
            context.saveStopwatches(updatedList)
        }
    }

    fun RemoveStopwatchById(itemId: Int) {
        viewModelScope.launch {
            val updatedList = _stopwatchList.value.filterNot { it.id == itemId }
            context.saveStopwatches(updatedList)
        }
    }

    fun timeLoop() {
        val updatedList = _stopwatchList.value.map { timer ->
            if (timer.isRunning) {
                timer.copy(time = timer.time + 1)
            } else {
                timer
            }
        }
        // We don't save the updated list to DataStore here to avoid excessive writes.
        // The user can save the state manually if needed.
        _stopwatchList.value = updatedList
    }
}