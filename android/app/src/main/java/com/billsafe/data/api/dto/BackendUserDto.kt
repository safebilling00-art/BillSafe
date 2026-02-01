package com.billsafe.data.api.dto

data class BackendUserDto(
    val uid: String,
    val email: String,
    val name: String? = null,
    val phoneNumber: String? = null,
    val currency: String? = null,
    val notificationsEnabled: Boolean? = null,
    val fcmToken: String? = null
)

