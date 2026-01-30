package com.billsafe.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userId: String,
    val email: String,
    val name: String,
    val phoneNumber: String = "",
    val currency: String = "INR",
    val notificationsEnabled: Boolean = true,
    val encryptionEnabled: Boolean = true
)
