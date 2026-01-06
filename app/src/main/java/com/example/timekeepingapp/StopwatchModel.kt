package com.example.timekeepingapp

data class StopwatchModel(val id: Int, var isRunning: Boolean = false,
                          var time: Long = 0L, var reset_time: Long = 0L,
                          var label: String = "Stopwatch ${id+1}")

//class StopwatchRepository {

    //private var _stopwatch = StopwatchModel(0L, false)

    //fun getStopwatch() = _stopwatch
//}