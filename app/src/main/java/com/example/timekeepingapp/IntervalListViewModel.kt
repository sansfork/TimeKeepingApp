package com.example.timekeepingapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.plus

class IntervalListViewModel: ViewModel() {

    private val _intervalList = MutableStateFlow(listOf(Interval(0)))

    private var _size = 1

    val intervalList = _intervalList.asStateFlow()

    fun get(itemIndex: Int): Interval {
        return intervalList.value[itemIndex]
    }

    fun AddInterval(newInterval: Interval) {
        _intervalList.value += newInterval
        _size += 1
    }

    fun RemoveIntervalById(itemId: Int) {
        _intervalList.update {
                list -> list.filterNot { it.id == itemId }
        }
        _size -= 1
    }

    fun timeLoop() {
        _intervalList.update { intervals ->
            // Check if any interval timer is running, and update their time if so
            intervals.map { interval ->
                if (!interval.isRunning) return@map interval

                // Work time
                if (interval.isWorking) {
                    if (interval.workTime > 0) {
                        return@map interval.copy(
                            workTime = interval.workTime - 1
                        )
                    }
                    return@map interval.copy(isWorking = false)
                }

                // Break time
                if (!interval.isWorking) {
                    if (interval.breakTime > 0) {
                        return@map interval.copy(
                            breakTime = interval.breakTime - 1
                        )
                    }

                    // Reset time and move to next set
                    val finished = interval.setsDone+1 >= interval.sets
                    if (finished) {
                        return@map interval.copy(
                            isWorking = true,
                            workTime = interval.reset_work,
                            breakTime = interval.reset_break,
                            isRunning = !finished
                        )
                    }

                    val nextSet = interval.setsDone + 1

                    interval.workTime = interval.reset_work
                    interval.breakTime = interval.reset_break

                    return@map interval.copy(
                        isWorking = true,
                        setsDone = nextSet,
                        workTime = interval.reset_work,
                        breakTime = interval.reset_break,
                        isRunning = !finished
                    )
                }
                interval
            }
        }
    }
}