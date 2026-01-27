package com.example.timekeepingapp

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {

// Keys for Group Mode

    // From GroupScreen
    val Group_idTracker = intPreferencesKey("group_idTracker") // Same IDs as ProfileTimeViewModel IDs
    // From GroupListViewModel
    val GroupItem_List = stringPreferencesKey("groupItem_list")

    // From ProfileScreen
    val ProfileScreen_timeIdTracker = intPreferencesKey("profileScreen_idTracker") // Tracks IDs for timers made for a specific profile
    // From ProfileTimeViewModel
    fun ProfileTimeList_Size(vmID: Int) = stringPreferencesKey("profileTimeList_size_$vmID") // Save the number of timers made for a specific profile
    // From ProfileTimeViewModel
    fun ProfileTime_List(vmID: Int) = stringPreferencesKey("profileTime_list_$vmID") // Save the timers made for a specific profile

// Keys for Personal Mode

    // From PersonalScreen
    val PersonalTimer_idTracker = intPreferencesKey("personalTimer_idTracker")
    // From TimerListViewModel
    val TimerList_Size = intPreferencesKey("timerList_size")
    // From TimerListViewModel
    val Timer_List = stringPreferencesKey("timer_list")

    // From PersonalScreen
    val PersonalStopwatch_idTracker = intPreferencesKey("personalStopwatch_idTracker")
    // From StopwatchListViewModel
    val StopwatchList_Size = intPreferencesKey("stopwatchList_size")
    // From StopwatchListViewModel
    val Stopwatch_List = stringPreferencesKey("stopwatch_list")

    // From PersonalScreen
    val PersonalInterval_idTracker = intPreferencesKey("personalInterval_idTracker")
    // From IntervalListViewModel
    val IntervalList_Size = intPreferencesKey("intervalList_size")
    // From IntervalListViewModel
    val Interval_List = stringPreferencesKey("interval_list")
}