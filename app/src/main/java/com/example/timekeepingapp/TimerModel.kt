package com.example.timekeepingapp

data class TimerModel(var time: Long, var isRunning: Boolean)

class TimerRepository {

    private var _timer = TimerModel(30L, false)

    fun getTimer() = _timer
}