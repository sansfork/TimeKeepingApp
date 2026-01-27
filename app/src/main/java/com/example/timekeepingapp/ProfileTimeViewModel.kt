package com.example.timekeepingapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.Math.floorDiv
import kotlin.collections.plus

class ProfileTimeViewModel(id: Int): ViewModel() {

    val vmId = id
    var missedSets: Int = 0

    private val _timeList = MutableStateFlow<List<Time>>(emptyList())

    private var _size = 0

    var size = _size

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

    fun tickTimer(timer: Timer, elapsedTime: Long): Timer {
        if (!timer.isRunning) return timer

        val timeDiff = GetRemainingTime(elapsedTime, timer.timeStamp)
        if (timeDiff > 0L) {
            if (timer.time - floorDiv(timeDiff, 1000) < 0L) {
                timer.time = 0L
            } else {
                timer.time -= (floorDiv(timeDiff, 1000))
            }
        }

        if (timer.time > 0) {
            timer.timeStamp = System.currentTimeMillis()
            return timer.copy(time = timer.time - 1)
        } else {
            return timer.copy(isRunning = false)
        }
    }

    fun tickStopwatch(stopwatch: Stopwatch, elapsedTime: Long): Stopwatch {
        val timeDiff = GetRemainingTime(elapsedTime, stopwatch.timeStamp)
        if (timeDiff > 0L) {
            stopwatch.time += (floorDiv(timeDiff, 1000))
        }

        if (stopwatch.isRunning) {
            stopwatch.timeStamp = System.currentTimeMillis()
            return stopwatch.copy(time = stopwatch.time + 1)
        } else {
            return stopwatch
        }
    }

    fun tickInterval(interval: Interval, elapsedTime: Long): Interval {
        if (!interval.isRunning) return interval

        if (elapsedTime > interval.timeStamp && interval.timeStamp != 0L) {
            interval.overflowDiff = floorDiv(GetRemainingTime(elapsedTime, interval.timeStamp), 1000)

            if (interval.isWorking) {
                interval.overflowDiff -= (interval.workTime + interval.breakTime)
                val result = DivideLongWithRemainder(interval.overflowDiff, interval.reset_work + interval.reset_break)
                missedSets = result.first
                interval.overflowDiff = result.second

                if (interval.setsDone+missedSets > interval.sets) {
                    interval.setsDone = interval.sets
                    interval.isWorking = false
                    interval.breakTime = 0L
                }

                if (interval.reset_work > interval.overflowDiff) {
                    interval.workTime = interval.overflowDiff
                    interval.overflowDiff = 0L
                } else {
                    interval.breakTime = interval.overflowDiff
                    interval.overflowDiff = 0L
                    interval.isWorking = false
                }
            } else {
                interval.overflowDiff -= (interval.breakTime)
                val result = DivideLongWithRemainder(interval.overflowDiff, interval.reset_work + interval.reset_break)
                missedSets = result.first
                interval.overflowDiff = result.second

                if (interval.setsDone+missedSets > interval.sets) {
                    interval.setsDone = interval.sets
                    interval.isWorking = false
                    interval.breakTime = 0L
                }

                if (interval.reset_work > interval.overflowDiff) {
                    interval.workTime = interval.overflowDiff
                    interval.overflowDiff = 0L
                } else {
                    interval.breakTime = interval.overflowDiff
                    interval.overflowDiff = 0L
                    interval.isWorking = false
                }
            }
        }

        // Work time
        if (interval.isWorking) {
            if (interval.workTime > 0) {
                interval.timeStamp = System.currentTimeMillis()
                return interval.copy(workTime = interval.workTime - 1)
            }
            return interval.copy(isWorking = false)
        }

        // Break time
        if (!interval.isWorking) {
            if (interval.breakTime > 0L) {
                interval.timeStamp = System.currentTimeMillis()
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

    fun timeLoop(elapsedTime: Long) {
        _timeList.update { timers ->
            // Check if any timer is running, and update their time if so
            timers.map { timer ->
                when (timer) {
                    is Timer -> tickTimer(timer, elapsedTime)
                    is Stopwatch -> tickStopwatch(timer, elapsedTime)
                    is Interval -> tickInterval(timer, elapsedTime)
                }
            }
        }
    }
}