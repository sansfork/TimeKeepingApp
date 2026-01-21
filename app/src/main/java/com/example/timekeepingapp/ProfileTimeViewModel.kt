package com.example.timekeepingapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.plus

class ProfileTimeViewModel: ViewModel() {

    private val _timeList = MutableStateFlow<List<Time>>(emptyList())

    private var _size = 0

    val timeList = _timeList.asStateFlow()

    fun get(itemIndex: Int): Time {
        return timeList.value[itemIndex]
    }

    fun AddTimer(newTimer: Time) {
        _timeList.value += newTimer
        _size += 1
    }

    fun RemoveTimerById(itemId: Int) {
        _timeList.update {
                list -> list.filterNot { it.id == itemId }
        }
        _size -= 1
    }

    fun tickTimer(timer: Timer): Timer {
        if (!timer.isRunning) return timer

        if (timer.time > 0) {
            return timer.copy(time = timer.time - 1)
        } else {
            return timer.copy(isRunning = false)
        }
    }

    fun tickStopwatch(stopwatch: Stopwatch): Stopwatch {
        if (stopwatch.isRunning) {
            return stopwatch.copy(time = stopwatch.time + 1)
        } else {
            return stopwatch
        }
    }

    fun tickInterval(interval: Interval): Interval {
        if (!interval.isRunning) return interval

        // Work time
        if (interval.isWorking) {
            if (interval.workTime > 0) {
                return interval.copy(workTime = interval.workTime - 1)
            }
            return interval.copy(isWorking = false)
        }

        // Break time
        if (!interval.isWorking) {
            if (interval.breakTime > 0) {
                return interval.copy(breakTime = interval.breakTime - 1)
            }

            // Reset time and move to next set
            val finished = interval.setsDone+1 >= interval.sets
            if (finished) {
                return interval.copy(
                    isWorking = true,
                    workTime = interval.reset_work,
                    breakTime = interval.reset_break,
                    isRunning = !finished
                )
            }

            val nextSet = interval.setsDone + 1

            interval.workTime = interval.reset_work
            interval.breakTime = interval.reset_break

            return interval.copy(
                isWorking = true,
                setsDone = nextSet,
                workTime = interval.reset_work,
                breakTime = interval.reset_break,
                isRunning = !finished
            )
        }
        return interval
    }

    fun timeLoop() {
        _timeList.update { timers ->
            // Check if any timer is running, and update their time if so
            timers.map { timer ->
                when (timer) {
                    is Timer -> tickTimer(timer)
                    is Stopwatch -> tickStopwatch(timer)
                    is Interval -> tickInterval(timer)
                }
            }
        }
    }
}