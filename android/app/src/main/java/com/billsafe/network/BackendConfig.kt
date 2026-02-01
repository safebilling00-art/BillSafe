package com.billsafe.network

import android.os.Build
import com.billsafe.BuildConfig

object BackendConfig {

    fun apiBaseUrl(): String {
        if (!BuildConfig.DEBUG) return BuildConfig.API_BASE_URL

        // Debug builds:
        // - Emulator: host machine is reachable at 10.0.2.2
        // - Physical device: prefer adb reverse -> device localhost maps to host port
        return if (isProbablyEmulator()) {
            "http://10.0.2.2:5000/"
        } else {
            "http://127.0.0.1:5000/"
        }
    }

    private fun isProbablyEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.startsWith("unknown") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.MANUFACTURER.contains("Genymotion") ||
            (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
            Build.PRODUCT == "google_sdk")
    }
}

