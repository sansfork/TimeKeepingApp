package com.example.timekeepingapp

import android.text.format.DateUtils.formatElapsedTime
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay



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

@Composable
fun StopwatchScreen() {
    var time by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(15)
            time += 1
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TimerText(time = time)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StartStopButton(isRunning = isRunning) {
                isRunning = !isRunning
            }
            ResetButton(onResetClick = {
                isRunning = false
                time = 0L
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StopwatchPreview() {
    StopwatchScreen()
}
