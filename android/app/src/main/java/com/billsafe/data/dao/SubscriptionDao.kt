package com.billsafe.data.dao

import androidx.room.*
import com.billsafe.data.entities.Subscription
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: Subscription)

    @Update
    suspend fun updateSubscription(subscription: Subscription)

    @Delete
    suspend fun deleteSubscription(subscription: Subscription)

    @Query("SELECT * FROM subscriptions WHERE userId = :userId AND status = 'active' ORDER BY renewalDate ASC")
    fun getActiveSubscriptions(userId: String): Flow<List<Subscription>>

    @Query("SELECT * FROM subscriptions WHERE userId = :userId AND LOWER(appName) = LOWER(:appName) AND status = 'active' LIMIT 1")
    suspend fun getActiveSubscriptionByAppName(userId: String, appName: String): Subscription?

    @Query("SELECT * FROM subscriptions WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllSubscriptions(userId: String): Flow<List<Subscription>>

    @Query("SELECT * FROM subscriptions WHERE id = :subscriptionId")
    suspend fun getSubscriptionById(subscriptionId: Int): Subscription?

    @Query("SELECT * FROM subscriptions WHERE userId = :userId AND isUsed = 0 AND status = 'active'")
    fun getUnusedSubscriptions(userId: String): Flow<List<Subscription>>

    @Query("SELECT SUM(amount) FROM subscriptions WHERE userId = :userId AND status = 'active'")
    suspend fun getTotalActiveSubscriptionAmount(userId: String): Double
}
