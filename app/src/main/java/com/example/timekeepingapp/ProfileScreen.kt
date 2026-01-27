package com.example.timekeepingapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(id: Int, navigationToGroupScreen:() -> Unit,
                  viewGroupList: GroupListViewModel, viewProfileTime: ProfileTimeViewModel) {

    val activity = LocalActivity.current as MainActivity
    var showFingerprint by remember { mutableStateOf(false) }

    val activityContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val timeId by activityContext.profileTimeIdTrackerFlow().collectAsState(initial = 0)

    val profile = viewGroupList.GetItemById(id)
    val listTime by viewProfileTime.timeList.collectAsState()

    val timePages = rememberPagerState(initialPage = 0, pageCount = { listTime.size })

    var timerMinutes by remember { mutableStateOf("0") }
    var timerSeconds by remember { mutableStateOf("30") }

    var breakMinutes by remember { mutableStateOf("0") }
    var breakSeconds by remember { mutableStateOf("30") }
    var intervalSets by remember { mutableStateOf("2") }

    var showDropdown by remember { mutableStateOf(false) }

    var showTimerDialog by remember { mutableStateOf(false) }
    var showIntervalDialog by remember { mutableStateOf(false) }

    val elapsedTime = System.currentTimeMillis()

    LaunchedEffect(viewProfileTime) {
        //if (GetRemainingTime(elapsedTime, listTime.any { it.startTimeStamp }) }) > 0)
        while (true) {
            if (listTime.any { it.isRunning }) {
                viewProfileTime.timeLoop(elapsedTime)
            }
            delay(1000)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp, horizontal = 16.dp)
                .background(Color.White.copy(alpha = 0.8f))
                .border(
                    border = BorderStroke(width = 2.dp, color = Color(0XFF018786)),
                    shape = RoundedCornerShape(0),
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(imageVector = Icons.Default.AccountCircle,
                contentDescription = null, modifier = Modifier.size(60.dp).padding(8.dp))
            Text("${profile?.name}", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
        }
        Column(
            modifier = Modifier.fillMaxSize().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp)
                    .background(Color.White.copy(alpha = 0.8f))
                    .border(
                        border = BorderStroke(width = 2.dp, color = Color(0XFF018786)),
                        shape = RoundedCornerShape(0),
                    ),
            ) {
                VerticalPager(timePages) {
                    page ->
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        ProfileTimeScreen(listTime[page])
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                navigationToGroupScreen()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Button(onClick = {
                //Toast.makeText(activityContext,
                //    "Whoopsies! You can't delete profiles yet! " +
                //    "(˶˃\uD800\uDCF7˂˶) (˶˃\uD800\uDCF7˂˶) (˶˃\uD800\uDCF7˂˶) (˶˃\uD800\uDCF7˂˶)",
                //    Toast.LENGTH_LONG).show()
                showFingerprint = true
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
            Button(onClick = {showDropdown = true}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = {showDropdown = false}
            ) {
                DropdownMenuItem(
                    text = {Text("Timer")},
                    onClick = {
                        showTimerDialog = true
                        showDropdown = false
                    }
                )
                DropdownMenuItem(
                    text = {Text("Stopwatch")},
                    onClick = {
                        viewProfileTime.AddTimer(Stopwatch(timeId))
                        coroutineScope.launch {
                            activityContext.saveProfileTimeIdTracker(timeId + 1)
                        }
                        showDropdown = false
                    }
                )
                DropdownMenuItem(
                    text = {Text("Interval")},
                    onClick = {
                        showIntervalDialog = true
                        showDropdown = false
                    }
                )
            }

            if (showFingerprint) {
                // trigger authentication directly
                LaunchedEffect(Unit) {
                    activity.startFingerprintAuth(
                        onSuccess = {
                            viewGroupList.RemoveItemById(id)
                            navigationToGroupScreen()
                            showFingerprint = false
                        },
                        onFail = {
                            showFingerprint = false
                        }
                    )
                }
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
                                    val newTimer = Timer(
                                        id = timeId,
                                        time = ConvertTimeToLong(timerMinutes, timerSeconds),
                                        reset_time = ConvertTimeToLong(timerMinutes, timerSeconds)
                                    )
                                    coroutineScope.launch {
                                        activityContext.saveProfileTimeIdTracker(timeId + 1)
                                    }
                                    viewProfileTime.AddTimer(newTimer)
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
                                    val newInterval = Interval(
                                        id = timeId,
                                        workTime = ConvertTimeToLong(timerMinutes, timerSeconds),
                                        breakTime = ConvertTimeToLong(breakMinutes, breakSeconds),
                                        sets = intervalSets.toInt(),
                                        reset_work = ConvertTimeToLong(timerMinutes, timerSeconds),
                                        reset_break = ConvertTimeToLong(breakMinutes, breakSeconds),
                                    )
                                    coroutineScope.launch {
                                        activityContext.saveProfileTimeIdTracker(timeId + 1)
                                    }
                                    viewProfileTime.AddTimer(newInterval)
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val application = LocalContext.current.applicationContext as Application
    ProfileScreen(0, {},
        GroupListViewModel(application), ProfileTimeViewModel(0, application))
}