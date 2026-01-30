package com.billsafe.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billsafe.data.dao.SubscriptionDao
import com.billsafe.data.entities.Subscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val subscriptionDao: SubscriptionDao
) : ViewModel() {

    private val userId = "user_123" // TODO: Get from Firebase Auth

    val subscriptions: Flow<List<Subscription>> = subscriptionDao.getActiveSubscriptions(userId)

    suspend fun addSubscriptionIfNew(
        appName: String,
        amount: Double,
        billingCycle: String,
        paymentMethod: String = ""
    ): Boolean {
        val cleanedName = appName.trim()
        if (cleanedName.isBlank()) return false

        val existing = subscriptionDao.getActiveSubscriptionByAppName(userId = userId, appName = cleanedName)
        if (existing != null) return false

        val now = LocalDateTime.now()
        val cycle = billingCycle.trim().lowercase()
        val renewalDate = when (cycle) {
            "yearly" -> now.plusYears(1)
            else -> now.plusMonths(1)
        }

        subscriptionDao.insertSubscription(
            Subscription(
                userId = userId,
                appName = cleanedName,
                amount = amount,
                billingCycle = cycle,
                startDate = now,
                renewalDate = renewalDate,
                status = "active",
                isUsed = true,
                paymentMethod = paymentMethod
            )
        )
        return true
    }

    fun addSubscription(subscription: Subscription) {
        viewModelScope.launch {
            addSubscriptionIfNew(
                appName = subscription.appName,
                amount = subscription.amount,
                billingCycle = subscription.billingCycle,
                paymentMethod = subscription.paymentMethod
            )
        }
    }

    fun cancelSubscription(subscription: Subscription) {
        viewModelScope.launch {
            subscriptionDao.updateSubscription(subscription.copy(status = "cancelled"))
        }
    }
}
