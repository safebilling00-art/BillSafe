package com.billsafe.data.api.dto

data class CreateUserRequest(
    val email: String,
    val name: String? = null,
    val phoneNumber: String? = null,
    val fcmToken: String? = null
)

