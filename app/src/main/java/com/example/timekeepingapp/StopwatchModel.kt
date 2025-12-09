package com.example.timekeepingapp

data class StopwatchModel(var time: Long, var isRunning: Boolean)

class StopwatchRepository {

    private var _stopwatch = StopwatchModel(0L, false)

    fun getStopwatch() = _stopwatch
}