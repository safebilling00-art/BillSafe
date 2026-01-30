package com.billsafe.data.dao

import androidx.room.*
import com.billsafe.data.entities.Bill
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: Bill)

    @Update
    suspend fun updateBill(bill: Bill)

    @Delete
    suspend fun deleteBill(bill: Bill)

    @Query("SELECT * FROM bills WHERE userId = :userId AND isActive = 1 ORDER BY dueDate ASC")
    fun getActiveBillsByUser(userId: String): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllBillsByUser(userId: String): Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE id = :billId")
    suspend fun getBillById(billId: Int): Bill?

    @Query("SELECT * FROM bills WHERE userId = :userId AND category = :category AND isActive = 1")
    fun getBillsByCategory(userId: String, category: String): Flow<List<Bill>>

    @Query("SELECT SUM(amount) FROM bills WHERE userId = :userId AND isActive = 1 AND frequency = 'monthly'")
    suspend fun getTotalMonthlyAmount(userId: String): Double
}
