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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.timekeepingapp.ui.theme.TimeKeepingAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewTimerList: TimerListViewModel by viewModels()
            val viewStopwatchList: StopwatchListViewModel by viewModels()
            val viewIntervalList: IntervalListViewModel by viewModels()
            val viewGroupList: GroupListViewModel by viewModels()
            val viewProfileTime: ProfileTimeViewModel by viewModels()
            TimeKeepingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenDisplay(
                        modifier = Modifier.padding(innerPadding),
                        viewTimerList,
                        viewStopwatchList,
                        viewIntervalList,
                        viewGroupList,
                        viewProfileTime,
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenDisplay(modifier: Modifier,
                  viewTimerList: TimerListViewModel,
                  viewStopwatchList: StopwatchListViewModel,
                  viewIntervalList: IntervalListViewModel,
                  viewGroupList: GroupListViewModel,
                  viewProfileTime: ProfileTimeViewModel
) {
    MyApp(viewTimerList, viewStopwatchList, viewIntervalList, viewGroupList, viewProfileTime)
}
// Fingerprint Scanner when deleting a profile (to fulfil the course requirement of using a phone sensor)
@Composable
fun MyApp(viewTimerList: TimerListViewModel, viewStopwatchList: StopwatchListViewModel,
          viewIntervalList: IntervalListViewModel, viewGroupList: GroupListViewModel,
          viewProfileTime: ProfileTimeViewModel,
) {
    val navController = rememberNavController()

    // Stopwatch LaunchedEffect(s)
    val stopwatches by viewStopwatchList.stopwatchList.collectAsState()
    LaunchedEffect(viewStopwatchList) {
        while (true) {
            if (stopwatches.any { it.isRunning }) {
                viewStopwatchList.timeLoop()
            }
            delay(1000)
        }
    }

    // Timer LaunchedEffect
    val timers by viewTimerList.timerList.collectAsState()
    LaunchedEffect(viewTimerList) {
        while (true) {
            if (timers.any { it.isRunning }) {
                viewTimerList.timeLoop()
            }
            delay(1000)
        }
    }

    // Interval LaunchedEffect(s)
    val intervals by viewIntervalList.intervalList.collectAsState()
    LaunchedEffect(viewIntervalList) {
        while (true) {
            if (intervals.any { it.isRunning }) {
                viewIntervalList.timeLoop()
            }
            delay(1000)
        }
    }

    val times by viewProfileTime.timeList.collectAsState()
    LaunchedEffect(viewProfileTime) {
        while (true) {
            if (times.any { it.isRunning }) {
                viewProfileTime.timeLoop()
            }
            delay(1000)
        }
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
                {navController.popBackStack()},
                {id -> navController.navigate("profilescreen/$id")},
                viewGroupList
            )
        }
        composable("personalscreen") {
            PersonalScreen(
                {navController.popBackStack() },
                viewTimerList,
                viewStopwatchList,
                viewIntervalList
            )
        }
        composable("profilescreen/{id}",
                    listOf(navArgument("id") { type = NavType.IntType})
        ) {
            backStackEntry ->
            val profileId = backStackEntry.arguments!!.getInt("id")

            ProfileScreen(
                profileId,
                {navController.popBackStack()},
                viewGroupList,
                viewProfileTime
            )
        }
    }
}