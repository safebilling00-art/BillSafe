package com.billsafe

import android.app.Application
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BillSafeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize encrypted shared preferences for secure data storage
        initializeSecureStorage()
    }

    private fun initializeSecureStorage() {
        try {
            val masterKey = MasterKey.Builder(this)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val encryptedSharedPreferences = EncryptedSharedPreferences.create(
                this,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
