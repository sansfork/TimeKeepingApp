package com.example.timekeepingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.timekeepingapp.ui.theme.TimeKeepingAppTheme
import kotlinx.coroutines.delay
import java.util.concurrent.Executor
import kotlin.collections.emptyList

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val listProfileTime = mutableListOf<ProfileTimeViewModel>()

            val viewTimerList: TimerListViewModel by viewModels()
            val viewStopwatchList: StopwatchListViewModel by viewModels()
            val viewIntervalList: IntervalListViewModel by viewModels()
            val viewGroupList: GroupListViewModel by viewModels()
            //val viewProfileTime: ProfileTimeViewModel by viewModels()
            TimeKeepingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenDisplay(
                        modifier = Modifier.padding(innerPadding),
                        viewTimerList,
                        viewStopwatchList,
                        viewIntervalList,
                        viewGroupList,
                        listProfileTime,
                    )
                }
            }
        }
    }
}

@Composable
fun FingerprintAuthentication(canAuthenticate: Boolean = true, onAuthSuccess: () -> Unit) {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val executor: Executor = ContextCompat.getMainExecutor(activity)

    val biometricPrompt = remember {
        BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(context, "Fingerprint Confirmed, Deletion Successful", Toast.LENGTH_SHORT).show()
                    onAuthSuccess()
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Fingerprint Not Recognized, Deletion Cancelled", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(context, "Error: $errString", Toast.LENGTH_SHORT).show()
                }
            })
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Confirmation")
            .setSubtitle("Scan Finger to Delete Profile")
            .setNegativeButtonText("Cancel")
            .build()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.baseline_fingerprint_24),
            contentDescription = "Fingerprint Icon",
            modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (canAuthenticate) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                Toast.makeText(context, "Fingerprint Scan Unavailable", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("SCAN FINGERPRINT")
        }
    }
}

@Composable
fun ScreenDisplay(modifier: Modifier,
                  viewTimerList: TimerListViewModel,
                  viewStopwatchList: StopwatchListViewModel,
                  viewIntervalList: IntervalListViewModel,
                  viewGroupList: GroupListViewModel,
                  listProfileTime: MutableList<ProfileTimeViewModel>
) {
    MyApp(viewTimerList, viewStopwatchList, viewIntervalList, viewGroupList, listProfileTime)
}
// Fingerprint Scanner when deleting a profile (to fulfil the course requirement of using a phone sensor)
@Composable
fun MyApp(viewTimerList: TimerListViewModel, viewStopwatchList: StopwatchListViewModel,
          viewIntervalList: IntervalListViewModel, viewGroupList: GroupListViewModel,
          listProfileTime: MutableList<ProfileTimeViewModel>,
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
                viewGroupList,
                listProfileTime
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

            val viewProfileTime = listProfileTime.filter { profileId == it.vmId }[0]

            ProfileScreen(
                profileId,
                {navController.popBackStack()},
                viewGroupList,
                viewProfileTime,
                listProfileTime
            )
        }
    }
}