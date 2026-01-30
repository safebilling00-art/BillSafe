package com.billsafe.utils

import com.billsafe.data.entities.Bill
import java.time.LocalDateTime

object SmsParser {
    
    // Common bill keywords for Indian context
    private val billKeywords = mapOf(
        "electricity" to listOf("bill", "kwh", "unit", "payment", "electricity", "power"),
        "water" to listOf("water", "supply", "meter", "consumption", "water bill"),
        "phone" to listOf("airtel", "jio", "vodafone", "bsnl", "mobile", "recharge", "plan"),
        "otp" to listOf("otp", "code", "verify", "confirm"),
        "subscription" to listOf("subscription", "subscription expired", "renew", "payment due"),
        "credit_card" to listOf("credit card", "payment due", "outstanding", "minimum"),
        "internet" to listOf("broadband", "internet", "wifi", "connection", "isp")
    )

    fun parseSms(message: String): Bill? {
        val lowerMessage = message.lowercase()
        
        // Detect category
        var category = "other"
        for ((cat, keywords) in billKeywords) {
            if (keywords.any { lowerMessage.contains(it) }) {
                category = cat
                break
            }
        }

        // Extract amount (common Indian number formats)
        val amountRegex = Regex("""(?:Rs|INR|₹)\s*(\d+\.?\d*)""", RegexOption.IGNORE_CASE)
        val amountMatch = amountRegex.find(lowerMessage)
        val amount = amountMatch?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0

        // Extract due date or day number
        val dateRegex = Regex("""(\d{1,2})(?:st|nd|rd|th)?\s*(?:of\s*)?(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December)""", RegexOption.IGNORE_CASE)
        val dateMatch = dateRegex.find(message)
        val dueDate = dateMatch?.groupValues?.get(1)?.toIntOrNull() ?: 1

        // If we found meaningful data, create a bill
        if (amount > 0.0 || category != "other") {
            return Bill(
                userId = "", // Will be set by the receiver
                billName = extractBillName(message, category),
                amount = amount,
                dueDate = dueDate,
                category = category,
                frequency = "monthly",
                source = "sms"
            )
        }

        return null
    }

    private fun extractBillName(message: String, category: String): String {
        return when (category) {
            "electricity" -> "Electricity Bill"
            "water" -> "Water Bill"
            "phone" -> "Phone Recharge"
            "credit_card" -> "Credit Card Payment"
            "internet" -> "Internet Bill"
            "subscription" -> extractSubscriptionName(message)
            else -> "Bill"
        }
    }

    private fun extractSubscriptionName(message: String): String {
        val apps = listOf("netflix", "amazon", "hotstar", "jiocinema", "spotify", "youtube")
        for (app in apps) {
            if (message.lowercase().contains(app)) {
                return "${app.capitalize()} Subscription"
            }
        }
        return "Subscription"
    }
}
