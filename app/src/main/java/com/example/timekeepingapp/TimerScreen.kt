package com.example.timekeepingapp

import android.text.format.DateUtils.formatElapsedTime
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
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
import java.nio.file.WatchEvent

@Composable
fun TimerScreen(viewModel: TimerViewModel) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TimerText(time = viewModel.time.value)
        Text("Edit", modifier = Modifier.padding(vertical = 16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StartStopButton(isRunning = viewModel.isRunning.value) {
                viewModel.isRunning.value = !viewModel.isRunning.value
            }
            ResetButton(onResetClick = {
                viewModel.isRunning.value = false
                viewModel.time.value = 30L
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerPreview() {
    TimerScreen(TimerViewModel())
}