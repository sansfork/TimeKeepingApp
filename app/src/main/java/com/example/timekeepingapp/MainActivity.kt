package com.example.timekeepingapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.timekeepingapp.ui.theme.TimeKeepingAppTheme
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    // callbacks set by screens
    private var onAuthSuccess: (() -> Unit)? = null
    private var onAuthFail: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val executor = ContextCompat.getMainExecutor(this)

        // Initialize BiometricPrompt
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onAuthSuccess?.invoke()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onAuthFail?.invoke()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onAuthFail?.invoke()
                }
            })

        // Only allow biometric (fingerprint) for simplicity
        // If you want PIN fallback, remove negativeButtonText
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Confirmation")
            .setSubtitle("Scan Finger to Delete Profile")
            .setNegativeButtonText("Cancel") // works with BIOMETRIC_STRONG only
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        setContent {
            val viewTimerList: TimerListViewModel by viewModels()
            val viewStopwatchList: StopwatchListViewModel by viewModels()
            val viewIntervalList: IntervalListViewModel by viewModels()
            val viewGroupList: GroupListViewModel by viewModels()

            TimeKeepingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenDisplay(
                        modifier = Modifier.padding(innerPadding),
                        viewTimerList,
                        viewStopwatchList,
                        viewIntervalList,
                        viewGroupList,
                    )
                }
            }
        }
    }
    // Public function screens can call
    fun startFingerprintAuth(
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        onAuthSuccess = onSuccess
        onAuthFail = onFail
        biometricPrompt.authenticate(promptInfo)
    }
}

@Composable
fun ScreenDisplay(modifier: Modifier,
                  viewTimerList: TimerListViewModel,
                  viewStopwatchList: StopwatchListViewModel,
                  viewIntervalList: IntervalListViewModel,
                  viewGroupList: GroupListViewModel,
) {
    MyApp(viewTimerList, viewStopwatchList, viewIntervalList, viewGroupList)
}

@Composable
fun MyApp(viewTimerList: TimerListViewModel, viewStopwatchList: StopwatchListViewModel,
          viewIntervalList: IntervalListViewModel, viewGroupList: GroupListViewModel,
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

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

            val viewProfileTime: ProfileTimeViewModel = viewModel(
                factory = ProfileTimeViewModelFactory(application, profileId)
            )

            ProfileScreen(
                profileId,
                {navController.popBackStack()},
                viewGroupList,
                viewProfileTime
            )
        }
    }
}