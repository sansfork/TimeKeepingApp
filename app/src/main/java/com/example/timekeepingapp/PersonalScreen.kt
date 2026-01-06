package com.example.timekeepingapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val _timerPageLimit = 5 // Raise to 10
private val _stopwatchPageLimit = 5
private val _intervalPageLimit = 5

private var _timerId = 1
private var _stopwatchId = 1
private var _intervalId = 1


@Composable
fun PersonalScreen(navigationToChoiceScreen:() -> Unit,
                   viewTimerList: TimerListViewModel,
                   viewStopwatchList: StopwatchListViewModel,
                   viewIntervalList: IntervalListViewModel) {

    val activityContext = LocalContext.current

    val listTimers by viewTimerList.timerList.collectAsState()
    val listStopwatches by viewStopwatchList.stopwatchList.collectAsState()
    val listIntervals by viewIntervalList.intervalList.collectAsState()

    var showTimerDialog by remember { mutableStateOf(false) }
    var showIntervalDialog by remember { mutableStateOf(false) }

    var timerMinutes by remember { mutableStateOf("0") }
    var timerSeconds by remember { mutableStateOf("30") }

    var breakMinutes by remember { mutableStateOf("0") }
    var breakSeconds by remember { mutableStateOf("30") }
    var intervalSets by remember { mutableStateOf("2") }

    val personalPages = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val timerPages = rememberPagerState(initialPage = 0, pageCount = { listTimers.size })
    val stopwatchPages = rememberPagerState(initialPage = 0, pageCount = { listStopwatches.size })
    val intervalPages = rememberPagerState(initialPage = 0, pageCount = { listIntervals.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Personal Mode", fontSize = 24.sp, modifier = Modifier.padding(top = 28.dp))
        // ViewPager2 (Use separate files for each page) (For now, Composables in same place)
        HorizontalPager(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(16.dp),
            state = personalPages
        ) {
            page -> when (page) {
                // EXPERIMENTAL (Works well)
                0 -> VerticalPager(state = timerPages) {
                    page ->
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        TimerScreen(listTimers[page])
                    }
                }
                // EXPERIMENTAL (Start/Stop works)
                1 -> VerticalPager(state = stopwatchPages) {
                    page ->
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        StopwatchScreen(listStopwatches[page])
                    }
                }
                // Not Implemented
                2 -> VerticalPager(state = intervalPages) {
                    page ->
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        IntervalScreen(listIntervals[page])
                    }
                }
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            // Navigate back to ChoiceScreen
            Button(onClick = {
                navigationToChoiceScreen()
            }) {
                Text("Back")
            }
            // Create new Timer/Stopwatch/Interval based on HorizontalPager page
            Button(onClick = {
                // If User is on Timer Page
                if (personalPages.currentPage == 0) {

                    if (listTimers.size+1 > _timerPageLimit) {
                        Toast.makeText(
                            activityContext,
                            "Item Limit Reached (Max. $_timerPageLimit)",
                            Toast.LENGTH_LONG).show()
                    } else {
                        showTimerDialog = true
                    }

                // If User is on Stopwatch Page
                } else if (personalPages.currentPage == 1) {

                    if (listStopwatches.size+1 > _stopwatchPageLimit) {
                        Toast.makeText(
                            activityContext,
                            "Item Limit Reached (Max. $_stopwatchPageLimit)",
                            Toast.LENGTH_LONG).show()
                    } else {
                        viewStopwatchList.AddStopwatch(StopwatchModel(_stopwatchId))
                        _stopwatchId += 1
                    }

                // If User is on Interval Page
                } else if (personalPages.currentPage == 2){

                    if (listIntervals.size+1 > _intervalPageLimit) {
                        Toast.makeText(
                            activityContext,
                            "Item Limit Reached (Max. $_intervalPageLimit)",
                            Toast.LENGTH_LONG).show()
                    } else {
                        showIntervalDialog = true
                    }

                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }

            if (showTimerDialog) {
                AlertDialog(onDismissRequest = {showTimerDialog = false},
                    confirmButton = {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = {
                                if (timerSeconds.isNotBlank()) {
                                    val newTimer = TimerModel(
                                        id = _timerId,
                                        time = ConvertTimeToLong(timerMinutes, timerSeconds),
                                        reset_time = ConvertTimeToLong(timerMinutes, timerSeconds)
                                    )
                                    _timerId += 1
                                    viewTimerList.AddTimer(newTimer)
                                    showTimerDialog = false
                                    timerMinutes = "0"
                                    timerSeconds = "30"
                                }
                            }) {
                                Text("Add")
                            }
                            Button(onClick = {showTimerDialog = false}) {
                                Text("Cancel")
                            }
                        }
                    },
                    title = { Text("Add new Timer") },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = timerMinutes,
                                onValueChange = { input ->
                                    if (input.matches(Regex("\\d*"))) {
                                        timerMinutes = input
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.width(80.dp)
                            )
                            Text(":", modifier = Modifier.padding(8.dp))
                            OutlinedTextField(
                                value = timerSeconds,
                                onValueChange = { input ->
                                    if (input.matches(Regex("\\d*"))) {
                                        timerSeconds = input
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.width(80.dp)
                            )
                        }
                    },
                )
            }

            if (showIntervalDialog) {
                AlertDialog(onDismissRequest = {showIntervalDialog = false},
                    confirmButton = {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = {
                                if (timerSeconds.isNotBlank()) {
                                    val newInterval = IntervalModel(
                                        id = _intervalId,
                                        workTime = ConvertTimeToLong(timerMinutes, timerSeconds),
                                        breakTime = ConvertTimeToLong(breakMinutes, breakSeconds),
                                        sets = intervalSets.toInt(),
                                        reset_work = ConvertTimeToLong(timerMinutes, timerSeconds),
                                        reset_break = ConvertTimeToLong(breakMinutes, breakSeconds),
                                    )
                                    _intervalId += 1
                                    viewIntervalList.AddInterval(newInterval)
                                    showIntervalDialog = false
                                    timerMinutes = "0"
                                    timerSeconds = "60"
                                    breakMinutes = "0"
                                    breakSeconds = "30"
                                }
                            }) {
                                Text("Add")
                            }
                            Button(onClick = {showIntervalDialog = false}) {
                                Text("Cancel")
                            }
                        }
                    },
                    title = { Text("Add new Interval Timer") },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Work", modifier = Modifier.padding(8.dp))
                                OutlinedTextField(
                                    value = timerMinutes,
                                    onValueChange = { input ->
                                        if (input.matches(Regex("\\d*"))) {
                                            timerMinutes = input
                                        }
                                    },
                                    singleLine = true,
                                    modifier = Modifier.width(80.dp)
                                )
                                Text(":", modifier = Modifier.padding(8.dp))
                                OutlinedTextField(
                                    value = timerSeconds,
                                    onValueChange = { input ->
                                        if (input.matches(Regex("\\d*"))) {
                                            timerSeconds = input
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
                                Text("Break", modifier = Modifier.padding(8.dp))
                                OutlinedTextField(
                                    value = breakMinutes,
                                    onValueChange = { input ->
                                        if (input.matches(Regex("\\d*"))) {
                                            breakMinutes = input
                                        }
                                    },
                                    singleLine = true,
                                    modifier = Modifier.width(80.dp)
                                )
                                Text(":", modifier = Modifier.padding(8.dp))
                                OutlinedTextField(
                                    value = breakSeconds,
                                    onValueChange = { input ->
                                        if (input.matches(Regex("\\d*"))) {
                                            breakSeconds = input
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
                                Text("Sets:", modifier = Modifier.padding(8.dp))
                                OutlinedTextField(
                                    value = intervalSets,
                                    onValueChange = { input ->
                                        if (input.matches(Regex("\\d*"))) {
                                            intervalSets = input
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
}



@Preview(showBackground = true)
@Composable
fun PersonalPreview() {
    PersonalScreen({},
        viewTimerList = TimerListViewModel(),
        viewStopwatchList = StopwatchListViewModel(),
        viewIntervalList = IntervalListViewModel()
        )
}