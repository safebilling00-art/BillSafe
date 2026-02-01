package com.billsafe.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.billsafe.auth.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val userSession: UserSession
) : ViewModel() {
    val firebaseUser = userSession.firebaseUser
    val uid = userSession.uid

    fun signOut() {
        userSession.signOut()
    }
}

