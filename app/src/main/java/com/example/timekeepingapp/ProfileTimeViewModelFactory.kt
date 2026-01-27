package com.example.timekeepingapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileTimeViewModelFactory(private val application: Application, private val vmId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileTimeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileTimeViewModel(vmId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}