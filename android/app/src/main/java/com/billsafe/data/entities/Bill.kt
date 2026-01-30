package com.billsafe.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val billName: String,
    val amount: Double,
    val dueDate: Int, // Day of month (1-31)
    val category: String, // electricity, water, phone, otp, subscription, etc.
    val frequency: String, // monthly, quarterly, yearly, one-time
    val description: String = "",
    val isActive: Boolean = true,
    val reminderDaysBefore: Int = 3,
    val lastPaidDate: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val source: String = "manual" // manual or sms or email
)
