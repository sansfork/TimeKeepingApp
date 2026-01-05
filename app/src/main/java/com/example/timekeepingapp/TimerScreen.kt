package com.example.timekeepingapp

import android.text.format.DateUtils.formatElapsedTime
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.nio.file.WatchEvent

@Composable
fun TimerScreen(timerModel: TimerModel) {

    var isEditing by remember { mutableStateOf(false) }
    var editMinutes by remember { mutableStateOf("0") }
    var editSeconds by remember { mutableStateOf("30") }
    var editLabel by remember { mutableStateOf(timerModel.label) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(timerModel.label)
        TimerText(time = timerModel.time)
        Button(onClick = {isEditing = true},
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent,
                contentColor = Color.Black)){
            Text("Edit")}
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StartStopButton(isRunning = timerModel.isRunning) {
                timerModel.isRunning = !timerModel.isRunning
            }
            ResetButton(onResetClick = {
                timerModel.isRunning = false
                timerModel.time = timerModel.reset_time
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
                            val newTime = ConvertTimeToLong(editMinutes, editSeconds)
                            timerModel.reset_time = newTime
                            // Reset timer
                            timerModel.isRunning = false
                            timerModel.time = timerModel.reset_time
                            // Change Label
                            timerModel.label = editLabel
                            // Close AlertDialog
                            isEditing = false
                        }) {
                            Text("Save")
                        }
                    }
                },
                title = { Text("Edit Timer") },
                text = {
                    Column{
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = editMinutes,
                                onValueChange = { input ->
                                    if (input.matches(Regex("\\d*"))) {
                                        editMinutes = input
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.width(80.dp)
                            )
                            Text(":", modifier = Modifier.padding(8.dp))
                            OutlinedTextField(
                                value = editSeconds,
                                onValueChange = { input ->
                                    if (input.matches(Regex("\\d*"))) {
                                        editSeconds = input
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
fun TimerPreview() {
    TimerScreen(TimerModel(0, 30L, 30L, false))
}