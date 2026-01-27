package com.example.timekeepingapp

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {

// Keys for Group Mode
    val GroupItem_List = stringPreferencesKey("groupItem_list")
    val Group_idTracker = intPreferencesKey("group_idTracker") // Same IDs as ProfileTimeViewModel IDs
    val GroupList_Size = intPreferencesKey("groupList_size")

    val ProfileScreen_timeIdTracker = intPreferencesKey("profileScreen_idTracker") // Tracks IDs for timers made for a specific profile

    fun ProfileTimeList_Size(vmID: Int) = stringPreferencesKey("profileTimeList_size_$vmID") // Save the number of timers made for a specific profile
    fun ProfileTime_List(vmID: Int) = stringPreferencesKey("profileTimeList_size_$vmID") // Save the timers made for a specific profile

// Keys for Personal Mode
    val PersonalTimer_idTracker = intPreferencesKey("personalTimer_idTracker")
    val TimerList_Size = intPreferencesKey("timerList_size")
    val Timer_List = stringPreferencesKey("timer_list")

    val PersonalStopwatch_idTracker = intPreferencesKey("personalStopwatch_idTracker")
    val StopwatchList_Size = intPreferencesKey("stopwatchList_size")
    val Stopwatch_List = stringPreferencesKey("stopwatch_list")

    val PersonalInterval_idTracker = intPreferencesKey("personalInterval_idTracker")
    val IntervalList_Size = intPreferencesKey("intervalList_size")
    val Interval_List = stringPreferencesKey("interval_list")
}