package com.billsafe.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.billsafe.data.entities.Bill
import com.billsafe.data.entities.Subscription
import com.billsafe.data.entities.User
import com.billsafe.data.dao.BillDao
import com.billsafe.data.dao.SubscriptionDao
import com.billsafe.data.dao.UserDao

@Database(
    entities = [Bill::class, Subscription::class, User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun userDao(): UserDao
}
