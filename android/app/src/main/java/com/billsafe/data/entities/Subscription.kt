package com.billsafe.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val appName: String,
    val amount: Double,
    val billingCycle: String, // monthly, yearly, etc.
    val startDate: LocalDateTime,
    val renewalDate: LocalDateTime,
    val status: String = "active", // active, cancelled, expired
    val isUsed: Boolean = true,
    val lastUsedDate: LocalDateTime? = null,
    val paymentMethod: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now()
)
