package com.example.timekeepingapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IntervalScreen(viewModel: IntervalViewModel) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Set ${viewModel.setsDone.value} of ${viewModel.sets.value}", modifier = Modifier.padding(8.dp))
        IntervalText(viewModel.workTime.value, viewModel.breakTime.value, viewModel.isWorking.value)
        Text(if (viewModel.isWorking.value) "Work" else "Break", modifier = Modifier.padding(8.dp))
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
                viewModel.workTime.value = 30L
                viewModel.breakTime.value = 20L
                viewModel.isWorking.value = true
                viewModel.setsDone.value = 0
                viewModel.sets.value = 2
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntervalPreview() {
    IntervalScreen(IntervalViewModel())
}