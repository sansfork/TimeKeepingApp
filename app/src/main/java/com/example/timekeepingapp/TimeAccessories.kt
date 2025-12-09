package com.example.timekeepingapp

import android.text.format.DateUtils.formatElapsedTime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerText(time: Long) {
    Text(
        text = formatElapsedTime(time),
        fontSize = 64.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun StopwatchText(time: Long) {
    Text(
        text = formatElapsedTime(time),
        fontSize = 64.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun StartStopButton(isRunning: Boolean, onStartStopClick: () -> Unit) {
    val label = if (isRunning) "Stop" else "Start"
    Button(onClick = onStartStopClick) {
        Text(label)
    }
}

@Composable
fun ResetButton(onResetClick: () -> Unit) {
    Button(onResetClick) {
        Text("Reset")
    }
}