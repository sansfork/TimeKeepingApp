package com.example.timekeepingapp

import androidx.compose.runtime.Composable

@Composable
fun ProfileTimeScreen(timer: Time) {
    when (timer) {
        is Timer -> TimerScreen(timer)
        is Stopwatch -> StopwatchScreen(timer)
        is Interval -> IntervalScreen(timer)
    }
}