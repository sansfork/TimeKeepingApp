package com.example.timekeepingapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.concurrent.timer

class TimerListViewModel: ViewModel() {

    private val _timerList = MutableStateFlow(listOf(Timer(0, time = 30L, reset_time = 30L)))

    private var _size = 1

    val timerList = _timerList.asStateFlow()

    fun get(itemIndex: Int): Timer {
        return timerList.value[itemIndex]
    }

    fun AddTimer(newTimer: Timer) {
        _timerList.value += newTimer
        _size += 1
    }

    fun RemoveTimerById(itemId: Int) {
        _timerList.update {
            list -> list.filterNot { it.id == itemId }
        }
        _size -= 1
    }

    fun timeLoop() {
        _timerList.update { timers ->
            // Check if any timer is running, and update their time if so
            timers.map { timer ->
                if (timer.isRunning && timer.time > 0) {
                    timer.copy(time = timer.time - 1)
                } else {
                    timer
                }
            }
        }
    }
}