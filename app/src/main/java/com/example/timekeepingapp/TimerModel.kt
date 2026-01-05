package com.example.timekeepingapp

data class TimerModel(val id: Int, var time: Long, var reset_time: Long, var isRunning: Boolean, var label: String = "Timer ${id+1}")

//class TimerRepository {

    //private var _timer = TimerModel(0, 30L, false)

    //fun getTimer() = _timer

    //fun getId() = _timer.id
//}