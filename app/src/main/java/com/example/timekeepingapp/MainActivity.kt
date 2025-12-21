package com.example.timekeepingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timekeepingapp.ui.theme.TimeKeepingAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewStopwatch: StopwatchViewModel by viewModels()
            val viewTimer: TimerViewModel by viewModels()
            val viewInterval: IntervalViewModel by viewModels()
            val viewGroupList: GroupListViewModel by viewModels()
            TimeKeepingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenDisplay(
                        modifier = Modifier.padding(innerPadding),
                        viewStopwatch,
                        viewTimer,
                        viewInterval,
                        viewGroupList,
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenDisplay(modifier: Modifier,
                  viewStopwatch: StopwatchViewModel,
                  viewTimer: TimerViewModel,
                  viewInterval: IntervalViewModel,
                  viewGroupList: GroupListViewModel,
) {
    MyApp(viewStopwatch, viewTimer, viewInterval, viewGroupList)
}
// Fingerprint Scanner when deleting a profile (to fulfil the course requirement of using a phone sensor)
@Composable
fun MyApp(viewStopwatch: StopwatchViewModel, viewTimer: TimerViewModel,
          viewInterval: IntervalViewModel, viewGroupList: GroupListViewModel,
) {
    val navController = rememberNavController()

    // Stopwatch LaunchedEffect(s)
    LaunchedEffect(viewStopwatch.isRunning.value) {
        while (viewStopwatch.isRunning.value) {
            delay(15)
            viewStopwatch.time.value += 1
        }
    }

    // Timer LaunchedEffect(s)
    LaunchedEffect(viewTimer.isRunning.value, viewTimer.time.value) {
        while (viewTimer.isRunning.value && viewTimer.time.value != 0L) {
            delay(15*60)
            viewTimer.time.value -= 1
        }
        viewTimer.isRunning.value = false
    }

    // Interval LaunchedEffect(s)
    LaunchedEffect(viewInterval.isRunning.value) {
        val workTime = viewInterval.workTime.value
        val breakTime = viewInterval.breakTime.value

        for (i in 1 until viewInterval.sets.value+1) {

            viewInterval.setsDone.value = i

            while (viewInterval.isRunning.value && viewInterval.workTime.value != 0L) {
                delay(15*60)
                viewInterval.workTime.value -= 1
            }
            viewInterval.isWorking.value = false

            while (viewInterval.isRunning.value && viewInterval.breakTime.value != 0L) {
                delay(15*60)
                viewInterval.breakTime.value -= 1
            }
            viewInterval.isWorking.value = true

            viewInterval.workTime.value = workTime
            viewInterval.breakTime.value = breakTime
        }
        viewInterval.isRunning.value = false
    }

    NavHost(navController, "choicescreen") {
        composable("choicescreen") {
            ChoiceScreen(
                {navController.navigate("groupscreen")},
                {navController.navigate("personalscreen")}
            )
        }
        composable("groupscreen") {
            GroupScreen(
                {navController.navigate("choicescreen")},
                {navController.navigate("profilescreen")},
                viewGroupList
            )
        }
        composable("personalscreen") {
            PersonalScreen(
                {navController.navigate("choicescreen") },
                viewStopwatch,
                viewTimer,
                viewInterval
            )
        }
        composable("profilescreen") {
            ProfileScreen(
                {navController.navigate("groupscreen")}
            )
        }
    }
}