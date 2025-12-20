package com.example.timekeepingapp

data class IntervalModel(var workTime: Long, var breakTime: Long, var sets: Int,
                         var setsDone: Int, var isWorking: Boolean,  var isRunning: Boolean)

class IntervalRepository {

    private var _timer = IntervalModel(30L, 20L, 2, 0,
        true, false)

    fun getTimer() = _timer
}