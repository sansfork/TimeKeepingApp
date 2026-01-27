package com.example.timekeepingapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class IntervalListViewModel(application: Application): AndroidViewModel(application) {

    private val _intervalList = MutableStateFlow<List<Interval>>(emptyList())

    val intervalList = _intervalList.asStateFlow()


    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    init {
        viewModelScope.launch {
            context.intervalFlow().collect { intervals ->
                _intervalList.value = intervals
            }
        }
    }

    fun get(itemIndex: Int): Interval {
        return intervalList.value[itemIndex]
    }

    fun AddInterval(newInterval: Interval) {
        viewModelScope.launch {
            val updatedList = _intervalList.value + newInterval
            context.saveIntervals(updatedList)
        }
    }

    fun RemoveIntervalById(itemId: Int) {
        viewModelScope.launch {
            val updatedList = _intervalList.value.filterNot { it.id == itemId }
            context.saveIntervals(updatedList)
        }
    }

    fun timeLoop() {
        val updatedList = _intervalList.value.map { interval ->
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
        // We don't save the updated list to DataStore here to avoid excessive writes.
        // The user can save the state manually if needed.
        _intervalList.value = updatedList
    }
}