package com.example.timekeepingapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val _timerPageLimit = 5
private val _stopwatchPageLimit = 5
private val _intervalPageLimit = 5

@Composable
fun PersonalScreen(navigationToChoiceScreen:() -> Unit,
                   viewStopwatch: StopwatchViewModel, viewTimer: TimerViewModel,
                   viewInterval: IntervalViewModel) {

    val activityContext = LocalContext.current

    var timerPageCount by remember { mutableIntStateOf(1) }
    var stopwatchPageCount by remember { mutableIntStateOf(1) }
    var intervalPageCount by remember { mutableIntStateOf(1) }

    val personalPages = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val timerPages = rememberPagerState(initialPage = 0, pageCount = { timerPageCount })
    val stopwatchPages = rememberPagerState(initialPage = 0, pageCount = { stopwatchPageCount })
    val intervalPages = rememberPagerState(initialPage = 0, pageCount = { intervalPageCount })

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
                        when (page) {
                            0 -> TimerScreen(viewTimer)
                            1 -> TimerScreen(viewTimer)
                            2 -> TimerScreen(viewTimer)
                            3 -> TimerScreen(viewTimer)
                            4 -> TimerScreen(viewTimer)
                            //5 -> TimerScreen(viewTimer)
                            //6 -> TimerScreen(viewTimer)
                            //7 -> TimerScreen(viewTimer)
                            //8 -> TimerScreen(viewTimer)
                            //9 -> TimerScreen(viewTimer)
                        }
                    }
                }
                // EXPERIMENTAL (Start/Stop works)
                1 -> VerticalPager(state = stopwatchPages) {
                    page ->
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        when (page) {
                            0 -> StopwatchScreen(viewStopwatch)
                            1 -> StopwatchScreen(viewStopwatch)
                            2 -> StopwatchScreen(viewStopwatch)
                            3 -> StopwatchScreen(viewStopwatch)
                            4 -> StopwatchScreen(viewStopwatch)
                        }
                    }
                }
                // Not Implemented
                2 -> VerticalPager(state = intervalPages) {
                    page ->
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        when (page) {
                            0 -> IntervalScreen(viewInterval)
                            1 -> IntervalScreen(viewInterval)
                            2 -> IntervalScreen(viewInterval)
                            3 -> IntervalScreen(viewInterval)
                            4 -> IntervalScreen(viewInterval)
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                navigationToChoiceScreen()
            }) {
                Text("Back")
            }
            Button(onClick = {
                if (personalPages.currentPage == 0) {

                    if (timerPageCount+1 > _timerPageLimit) {
                        Toast.makeText(
                            activityContext,
                            "Item Limit Reached (Max. $_timerPageLimit)",
                            Toast.LENGTH_LONG).show()
                    } else {
                        timerPageCount += 1
                    }

                } else if (personalPages.currentPage == 1) {

                    if (stopwatchPageCount+1 > _stopwatchPageLimit) {
                        Toast.makeText(
                            activityContext,
                            "Item Limit Reached (Max. $_stopwatchPageLimit)",
                            Toast.LENGTH_LONG).show()
                    } else {
                        stopwatchPageCount += 1
                    }

                } else if (personalPages.currentPage == 2){

                    if (intervalPageCount+1 > _intervalPageLimit) {
                        Toast.makeText(
                            activityContext,
                            "Item Limit Reached (Max. $_intervalPageLimit)",
                            Toast.LENGTH_LONG).show()
                    } else {
                        intervalPageCount += 1
                    }

                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }

    }
}
// HorizontalPager pages (to be moved to separate files)

@Preview(showBackground = true)
@Composable
fun PersonalPreview() {
    PersonalScreen({},
        viewStopwatch = StopwatchViewModel(),
        viewTimer = TimerViewModel(),
        viewInterval = IntervalViewModel()
        )
}