package com.billsafe.data.dao

import androidx.room.*
import com.billsafe.data.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: String): User?

    @Delete
    suspend fun deleteUser(user: User)
}
