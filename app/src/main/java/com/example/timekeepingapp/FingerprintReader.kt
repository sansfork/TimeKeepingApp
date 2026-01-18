package com.example.timekeepingapp

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun showFingerprintPrompt(activity: FragmentActivity, onConfirm: () -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)

    val fingerprintPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onConfirm()
            }
        }
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Are you sure you want to delete this profile?")
        .setDescription("Use Fingerprint To Confirm")
        .setNegativeButtonText("Cancel")
        .build()

    fingerprintPrompt.authenticate(promptInfo)
}