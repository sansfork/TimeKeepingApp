package com.example.timekeepingapp

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
    val json = Json.encodeToString(ListSerializer(PolymorphicSerializer(GroupItem::class)), groupItems)
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


suspend fun Context.saveProfileTime(vm: ProfileTimeViewModel) {
    dataStore.edit { prefs ->
        // Serialize the time list
        val json = timeJson.encodeToString(
            ListSerializer(PolymorphicSerializer(Time::class)),
            vm.timeList.value
        )
        prefs[DataStoreKeys.ProfileTime_List(vm.vmId)] = json
        prefs[DataStoreKeys.ProfileTimeList_Size(vm.vmId)] = vm.timeList.value.size.toString()
    }
}


suspend fun Context.loadProfile(vmId: Int): ProfileTimeViewModel {
    val vm = ProfileTimeViewModel(vmId)
    val prefs = dataStore.data.first()

    prefs[DataStoreKeys.ProfileTime_List(vmId)]?.let { json ->
        val list = timeJson.decodeFromString(
            ListSerializer(PolymorphicSerializer(Time::class)),
            json
        )
        list.forEach { vm.AddTimer(it) }
    }

    prefs[DataStoreKeys.ProfileTimeList_Size(vmId)]?.let { sizeStr ->
        val size = sizeStr.toIntOrNull() ?: vm.timeList.value.size
        // Update _size manually
        // (assuming you expose a function or make _size internal for saving)
        vm.size = size
    }

    return vm
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

suspend fun Context.saveGroupListSize(size: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.GroupList_Size] = size
    }
}

fun Context.groupListSizeFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.GroupList_Size] ?: 0
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

// Personal Mode Functions

suspend fun Context.savePersonalTimerIdTracker(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.PersonalTimer_idTracker] = id
    }
}

fun Context.personalTimerIdTrackerFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.PersonalTimer_idTracker] ?: 0
    }

suspend fun Context.savePersonalTimerListSize(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.TimerList_Size] = id
    }
}

fun Context.personalTimerListSizeFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.TimerList_Size] ?: 0
    }

val timerJson = Json { encodeDefaults = true; ignoreUnknownKeys = true }
suspend fun Context.saveTimers(list: List<Timer>) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.Timer_List] = timerJson.encodeToString(ListSerializer(Timer.serializer()), list)
    }
}

fun Context.timerFlow(): Flow<List<Timer>> = dataStore.data
    .map { prefs ->
        prefs[DataStoreKeys.Timer_List]?.let { json ->
            timerJson.decodeFromString<List<Timer>>(json)
        } ?: emptyList()
    }

suspend fun Context.savePersonalStopwatchIdTracker(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.PersonalStopwatch_idTracker] = id
    }
}

fun Context.personalStopwatchIdTrackerFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.PersonalStopwatch_idTracker] ?: 0
    }

suspend fun Context.savePersonalStopwatchListSize(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.StopwatchList_Size] = id
    }
}

fun Context.personalStopwatchListSizeFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.StopwatchList_Size] ?: 0
    }

val stopwatchJson = Json { encodeDefaults = true; ignoreUnknownKeys = true }
suspend fun Context.saveStopwatches(list: List<Stopwatch>) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.Stopwatch_List] = timerJson.encodeToString(ListSerializer(Stopwatch.serializer()), list)
    }
}

fun Context.stopwatchFlow(): Flow<List<Stopwatch>> = dataStore.data
    .map { prefs ->
        prefs[DataStoreKeys.Stopwatch_List]?.let { json ->
            stopwatchJson.decodeFromString<List<Stopwatch>>(json)
        } ?: emptyList()
    }


suspend fun Context.savePersonalIntervalIdTracker(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.PersonalInterval_idTracker] = id
    }
}

fun Context.personalIntervalIdTrackerFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.PersonalInterval_idTracker] ?: 0
    }

suspend fun Context.savePersonalIntervalListSize(id: Int) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.IntervalList_Size] = id
    }
}

fun Context.personalIntervalListSizeFlow(): Flow<Int> =
    dataStore.data.map { prefs ->
        prefs[DataStoreKeys.IntervalList_Size] ?: 0
    }

val intervalJson = Json { encodeDefaults = true; ignoreUnknownKeys = true }
suspend fun Context.saveIntervals(list: List<Interval>) {
    dataStore.edit { prefs ->
        prefs[DataStoreKeys.Interval_List] = timerJson.encodeToString(ListSerializer(Interval.serializer()), list)
    }
}

fun Context.intervalFlow(): Flow<List<Interval>> = dataStore.data
    .map { prefs ->
        prefs[DataStoreKeys.Interval_List]?.let { json ->
            intervalJson.decodeFromString<List<Interval>>(json)
        } ?: emptyList()
    }