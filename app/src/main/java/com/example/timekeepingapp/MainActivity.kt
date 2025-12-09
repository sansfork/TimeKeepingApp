package com.example.timekeepingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timekeepingapp.ui.theme.TimeKeepingAppTheme
import kotlinx.coroutines.delay
import java.util.Timer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewStopwatch: StopwatchViewModel by viewModels()
            val viewTimer: TimerViewModel by viewModels()
            TimeKeepingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenDisplay(
                        modifier = Modifier.padding(innerPadding),
                        viewStopwatch,
                        viewTimer,
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenDisplay(modifier: Modifier,
                  viewStopwatch: StopwatchViewModel,
                  viewTimer: TimerViewModel) {
    MyApp(viewStopwatch, viewTimer)
}

@Composable
fun MyApp(viewStopwatch: StopwatchViewModel, viewTimer: TimerViewModel) {
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

    NavHost(navController, "choicescreen") {
        composable("choicescreen") {
            ChoiceScreen(
                {navController.navigate("groupscreen")},
                {navController.navigate("personalscreen")}
            )
        }
        composable("groupscreen") {
            GroupScreen(
                {navController.navigate("choicescreen")}
            )
        }
        composable("personalscreen") {
            PersonalScreen(
                {navController.navigate("choicescreen") },
                viewStopwatch,
                viewTimer
            )
        }
    }
}