package com.example.timekeepingapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.plus

class StopwatchListViewModel: ViewModel() {

    private val _stopwatchList = MutableStateFlow(listOf(Stopwatch(0)))

    private var _size = 1

    val stopwatchList = _stopwatchList.asStateFlow()

    fun get(itemIndex: Int): Stopwatch {
        return stopwatchList.value[itemIndex]
    }

    fun AddStopwatch(newTimer: Stopwatch) {
        _stopwatchList.value += newTimer
        _size += 1
    }

    fun RemoveStopwatchById(itemId: Int) {
        _stopwatchList.update {
                list -> list.filterNot { it.id == itemId }
        }
        _size -= 1
    }

    fun timeLoop() {
        _stopwatchList.update { timers ->
            // Check if any stopwatch is running, and update their time if so
            timers.map { timer ->
                if (timer.isRunning) {
                    timer.copy(time = timer.time + 1)
                } else {
                    timer
                }
            }
        }
    }
}