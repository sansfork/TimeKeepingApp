package com.example.timekeepingapp

data class IntervalModel(val id: Int, var workTime: Long = 60L, var breakTime: Long = 30L, var sets: Int = 2,
                         var reset_work: Long = 60L, var reset_break: Long = 30L,
                         var setsDone: Int = 0, var isWorking: Boolean = true,  var isRunning: Boolean = false,
                         var label: String = "Interval ${id+1}")

//class IntervalRepository {

    //private var _timer = IntervalModel(30L, 20L, 2, 0,
    //    true, false)

    //fun getTimer() = _timer
//}