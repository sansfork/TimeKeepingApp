package com.example.timekeepingapp

import android.widget.Spinner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun PersonalScreen(navigationToChoiceScreen:() -> Unit,
                   viewStopwatch: StopwatchViewModel, viewTimer: TimerViewModel,
                   viewInterval: IntervalViewModel) {

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Personal Mode", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navigationToChoiceScreen()
        }) {
            Text("Back")
        }
        // ViewPager2 (Use separate files for each page) (For now, Composables in same place)
        HorizontalPager(state = pagerState) {
            page -> when (page) {
            // EXPERIMENTAL (Works well)
            0 -> TimerScreen(viewTimer)
            // EXPERIMENTAL (Start/Stop works)
            1 -> StopwatchScreen(viewStopwatch)
            // Not Implemented
            2 -> IntervalScreen(viewInterval)
            }
        }
    }
}
// HorizontalPager pages (to be moved to separate files)

@Composable
fun IntervalScreen() {
    Text("No Interval Timer Yet", modifier = Modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
fun PersonalPreview() {
    PersonalScreen({},
        viewStopwatch = StopwatchViewModel(),
        viewTimer = TimerViewModel(),
        viewInterval = IntervalViewModel()
        )
}