package com.example.timekeepingapp

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore("app_prefs")

// Group Mode Functions
val timeJson = Json {
    serializersModule = SerializersModule {
        polymorphic(Time::class) {
            subclass(Timer::class)
            subclass(Stopwatch::class)
            subclass(Interval::class)
        }
    }
    classDiscriminator = "type" // Adds a "type" field to distinguish Timer/Stopwatch/Interval
    prettyPrint = true
}

suspend fun Context.saveGroupItems(groupItems: List<GroupItem>) {
    val json = Json.encodeToString(ListSerializer(GroupItem.serializer()), groupItems)
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.GroupItem_List] = json
    }
}

fun Context.groupItemFlow(): Flow<List<GroupItem>> = dataStore.data
    .map { prefs ->
        prefs[DataStoreKeys.GroupItem_List]?.let { json ->
            Json.decodeFromString(
                ListSerializer(GroupItem.serializer()),
                json
            )
        } ?: emptyList()
    }

suspend fun Context.saveGroupIdTracker(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.Group_idTracker] = id
    }
}

fun Context.groupIdTrackerFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.Group_idTracker] ?: 0
    }

suspend fun Context.saveProfileTimeIdTracker(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.ProfileScreen_timeIdTracker] = id
    }
}

fun Context.profileTimeIdTrackerFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.ProfileScreen_timeIdTracker] ?: 0
    }

suspend fun Context.saveProfileTimeList(vmId: Int, timeList: List<Time>) {
    dataStore.edit { prefs ->
        val json = timeJson.encodeToString(ListSerializer(PolymorphicSerializer(Time::class)), timeList)
        prefs[DataStoreKeys.ProfileTime_List(vmId)] = json
        prefs[DataStoreKeys.ProfileTimeList_Size(vmId)] = timeList.size.toString()
    }
}

fun Context.profileTimeListFlow(vmId: Int): Flow<List<Time>> = dataStore.data
    .map { prefs ->
        prefs[DataStoreKeys.ProfileTime_List(vmId)]?.let { json ->
            timeJson.decodeFromString(
                ListSerializer(PolymorphicSerializer(Time::class)),
                json
            )
        } ?: emptyList()
    }


// Personal Mode Functions

// Timers Timers
suspend fun Context.savePersonalTimerIdTracker(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.PersonalTimer_idTracker] = id
    }
}

fun Context.personalTimerIdTrackerFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.PersonalTimer_idTracker] ?: 0
    }

val timerJson = Json { encodeDefaults = true; ignoreUnknownKeys = true }
suspend fun Context.saveTimers(list: List<Timer>) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.Timer_List] = timerJson.encodeToString(ListSerializer(Timer.serializer()), list)
        prefs[DataStoreKeys.TimerList_Size] = list.size
    }
}

fun Context.timerFlow(): Flow<List<Timer>> = dataStore.data
    .map { prefs ->
        prefs[DataStoreKeys.Timer_List]?.let { json ->
            timerJson.decodeFromString<List<Timer>>(json)
        } ?: emptyList()
    }

// Stopwatch Timers
suspend fun Context.savePersonalStopwatchIdTracker(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.PersonalStopwatch_idTracker] = id
    }
}

fun Context.personalStopwatchIdTrackerFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.PersonalStopwatch_idTracker] ?: 0
    }

val stopwatchJson = Json { encodeDefaults = true; ignoreUnknownKeys = true }
suspend fun Context.saveStopwatches(list: List<Stopwatch>) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.Stopwatch_List] = timerJson.encodeToString(ListSerializer(Stopwatch.serializer()), list)
        prefs[DataStoreKeys.StopwatchList_Size] = list.size
    }
}

fun Context.stopwatchFlow(): Flow<List<Stopwatch>> = dataStore.data
    .map { prefs ->
        prefs[DataStoreKeys.Stopwatch_List]?.let { json ->
            stopwatchJson.decodeFromString<List<Stopwatch>>(json)
        } ?: emptyList()
    }

// Interval Timers
suspend fun Context.savePersonalIntervalIdTracker(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.PersonalInterval_idTracker] = id
    }
}

fun Context.personalIntervalIdTrackerFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.PersonalInterval_idTracker] ?: 0
    }

val intervalJson = Json { encodeDefaults = true; ignoreUnknownKeys = true }
suspend fun Context.saveIntervals(list: List<Interval>) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.Interval_List] = timerJson.encodeToString(ListSerializer(Interval.serializer()), list)
        prefs[DataStoreKeys.IntervalList_Size] = list.size
    }
}

fun Context.intervalFlow(): Flow<List<Interval>> = dataStore.data
    .map { prefs ->
        prefs[DataStoreKeys.Interval_List]?.let { json ->
            intervalJson.decodeFromString<List<Interval>>(json)
        } ?: emptyList()
    }