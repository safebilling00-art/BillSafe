package com.billsafe.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billsafe.auth.UserSession
import com.billsafe.data.api.BillSafeApi
import com.billsafe.data.api.dto.CreateUserRequest
import com.billsafe.network.BackendConfig
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class UserSyncViewModel @Inject constructor(
    private val billSafeApi: BillSafeApi,
    private val userSession: UserSession
) : ViewModel() {

    private val _syncing = MutableStateFlow(false)
    val syncing: StateFlow<Boolean> = _syncing.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    fun syncCurrentUserToBackend() {
        val firebaseUser = userSession.firebaseUser.value ?: return
        val uid = firebaseUser.uid
        val email = firebaseUser.email ?: return

        viewModelScope.launch {
            if (_syncing.value) return@launch
            _syncing.value = true
            _lastError.value = null

            try {
                val fcmToken = getFcmTokenOrNull()
                Log.d(
                    "BillSafeFCM",
                    "Syncing user to backend uid=$uid baseUrl=${BackendConfig.apiBaseUrl()} hasFcmToken=${!fcmToken.isNullOrBlank()}"
                )
                val request = CreateUserRequest(
                    email = email,
                    name = firebaseUser.displayName,
                    phoneNumber = firebaseUser.phoneNumber,
                    fcmToken = fcmToken
                )

                val response = withContext(Dispatchers.IO) {
                    billSafeApi.createUser(uid = uid, request = request)
                }

                if (!response.success) {
                    val message = response.error ?: "Failed to sync user."
                    _lastError.value = message
                    Log.w("UserSync", "Backend user sync failed: $message")
                    Log.w("BillSafeFCM", "Backend user sync failed: $message")
                }
            } catch (e: Exception) {
                val message = e.message ?: "Failed to sync user."
                _lastError.value = message
                Log.w("UserSync", "Backend user sync failed", e)
                Log.w("BillSafeFCM", "Backend user sync failed", e)
            } finally {
                _syncing.value = false
            }
        }
    }

    private suspend fun getFcmTokenOrNull(): String? = suspendCancellableCoroutine { cont ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                cont.resume(null)
                return@addOnCompleteListener
            }
            cont.resume(task.result)
        }
    }
}
