package com.example.timekeepingapp

sealed interface Time {
    val id: Int
    var label: String
    var isRunning: Boolean
    var timeStamp: Long
}

data class Timer (
    override val id: Int,
    override var label: String = "Timer ${id+1}",
    override var isRunning: Boolean = false,
    var time: Long,
    var reset_time: Long,
    override var timeStamp: Long = 0L
): Time

data class Stopwatch (
    override val id: Int,
    override var label: String = "Stopwatch ${id+1}",
    override var isRunning: Boolean = false,
    var time: Long = 0L,
    var reset_time: Long = 0L,
    override var timeStamp: Long = 0L
): Time

data class Interval (
    override val id: Int,
    override var label: String = "Interval ${id+1}",
    override var isRunning: Boolean = false,
    var isWorking: Boolean = true,
    var workTime: Long = 60L,
    var breakTime: Long = 30L,
    var reset_work: Long = 60L,
    var reset_break: Long = 30L,
    var sets: Int = 2,
    var setsDone: Int = 0,
    override var timeStamp: Long = 0L,
    var overflowDiff: Long = 0L
): Time