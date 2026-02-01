package com.billsafe.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.billsafe.network.BackendConfig
import com.billsafe.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class BillSafeMessagingService : FirebaseMessagingService() {

    private val httpClient = OkHttpClient()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "BillSafe Reminder"
        val body = remoteMessage.notification?.body ?: "You have an upcoming bill"

        sendNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveFcmTokenToBackend(token)
    }

    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "bill_reminders",
                "Bill Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "bill_reminders")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun saveFcmTokenToBackend(token: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            Log.d("BillSafeFCM", "FCM token refreshed but user is not signed in; will sync on next login.")
            return
        }

        val email = firebaseUser.email
        if (email.isNullOrBlank()) {
            Log.d("BillSafeFCM", "FCM token refreshed but user has no email; skipping backend sync.")
            return
        }

        val url = BackendConfig.apiBaseUrl().trimEnd('/') + "/api/users/${firebaseUser.uid}"

        val payload = JSONObject().apply {
            put("email", email)
            put("name", firebaseUser.displayName ?: JSONObject.NULL)
            put("phoneNumber", firebaseUser.phoneNumber ?: JSONObject.NULL)
            put("fcmToken", token)
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val body = payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                httpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.w("BillSafeFCM", "Failed to sync FCM token to backend (${response.code})")
                    }
                }
            } catch (e: Exception) {
                Log.w("BillSafeFCM", "Failed to sync FCM token to backend", e)
            }
        }
    }
}
