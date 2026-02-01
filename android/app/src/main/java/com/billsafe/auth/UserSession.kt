package com.billsafe.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(firebaseAuth.currentUser)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser.asStateFlow()

    private val _uid = MutableStateFlow(firebaseAuth.currentUser?.uid)
    val uid: StateFlow<String?> = _uid.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val user = auth.currentUser
        _firebaseUser.value = user
        _uid.value = user?.uid
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}

