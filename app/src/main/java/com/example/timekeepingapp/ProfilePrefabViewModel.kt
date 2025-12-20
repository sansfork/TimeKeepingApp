package com.example.timekeepingapp

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import java.util.UUID

data class ProfilePrefab(
    val id: Int,
    val profileName: String
)

class ProfilePrefabViewModel(): ViewModel() {
    private val _profiles = mutableListOf<ProfilePrefab>()
    // val profiles = <ProfilePrefab> = _profiles
}
