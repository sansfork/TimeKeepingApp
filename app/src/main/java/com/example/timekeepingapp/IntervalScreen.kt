package com.example.timekeepingapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IntervalScreen(intervalModel: Interval) {

    var isEditing by remember { mutableStateOf(false) }
    var editWorkMinutes by remember { mutableStateOf("0") }
    var editWorkSeconds by remember { mutableStateOf("60") }
    var editBreakMinutes by remember { mutableStateOf("0") }
    var editBreakSeconds by remember { mutableStateOf("30") }
    var editSets by remember { mutableStateOf("2") }
    var editLabel by remember { mutableStateOf(intervalModel.label) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(intervalModel.label, fontSize = 20.sp)
        Text("Set ${intervalModel.setsDone+1} of ${intervalModel.sets}", modifier = Modifier.padding(8.dp))
        IntervalText(intervalModel.workTime, intervalModel.breakTime, intervalModel.isWorking)
        Text(if (intervalModel.isWorking) "Work" else "Break", modifier = Modifier.padding(8.dp))
        Button(onClick = {isEditing = true},
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent,
                contentColor = Color.Black)){
            Text("Edit", fontSize = 18.sp)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StartStopButton(isRunning = intervalModel.isRunning) {
                intervalModel.isRunning = !intervalModel.isRunning
            }
            ResetButton(onResetClick = {
                intervalModel.workTime = intervalModel.reset_work
                intervalModel.breakTime = intervalModel.reset_break
                intervalModel.sets = 2
                intervalModel.setsDone = 0
                intervalModel.isWorking = true
                intervalModel.isRunning = false
            })
        }

        if (isEditing) {
            AlertDialog(onDismissRequest = {isEditing = false},
                confirmButton = {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = {isEditing = false}) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            // Assign reset_time to newTime
                            val newWorkTime = ConvertTimeToLong(editWorkMinutes, editWorkSeconds)
                            val newBreakTime = ConvertTimeToLong(editBreakMinutes, editBreakSeconds)
                            intervalModel.reset_work = newWorkTime
                            intervalModel.reset_break = newBreakTime
                            // Reset timer
                            intervalModel.isRunning = false
                            intervalModel.workTime = intervalModel.reset_work
                            intervalModel.breakTime = intervalModel.reset_break
                            intervalModel.sets = editSets.toInt()
                            // Change Label
                            intervalModel.label = editLabel
                            // Close AlertDialog
                            isEditing = false
                        }) {
                            Text("Save")
                        }
                    }
                },
                title = { Text("Edit Interval") },
                text = {
                    Column{
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Label:", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                            BasicTextField(
                                value = editLabel,
                                onValueChange = {editLabel = it},
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 16.sp
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .wrapContentSize()
                                    .background(Color.LightGray)
                                    .padding(8.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Work:", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                            OutlinedTextField(
                                value = editWorkMinutes,
                                onValueChange = { input ->
                                    if (input.matches(Regex("\\d*"))) {
                                        editWorkMinutes = input
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.width(80.dp)
                            )
                            Text(":", modifier = Modifier.padding(8.dp))
                            OutlinedTextField(
                                value = editWorkSeconds,
                                onValueChange = { input ->
                                    if (input.matches(Regex("\\d*"))) {
                                        editWorkSeconds = input
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.width(80.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Break:", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                            OutlinedTextField(
                                value = editBreakMinutes,
                                onValueChange = { input ->
                                    if (input.matches(Regex("\\d*"))) {
                                        editBreakMinutes = input
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.width(80.dp)
                            )
                            Text(":", modifier = Modifier.padding(8.dp))
                            OutlinedTextField(
                                value = editBreakSeconds,
                                onValueChange = { input ->
                                    if (input.matches(Regex("\\d*"))) {
                                        editBreakSeconds = input
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.width(80.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sets:", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                            OutlinedTextField(
                                value = editSets,
                                onValueChange = { input ->
                                    if (input.matches(Regex("\\d*"))) {
                                        editSets = input
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.width(80.dp)
                            )
                        }
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntervalPreview() {
    IntervalScreen(Interval(0))
}