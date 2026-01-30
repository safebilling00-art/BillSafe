package com.billsafe.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billsafe.data.dao.BillDao
import com.billsafe.data.dao.SubscriptionDao
import com.billsafe.data.entities.Bill
import com.billsafe.data.entities.Subscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val billDao: BillDao,
    private val subscriptionDao: SubscriptionDao
) : ViewModel() {

    private val userId = "user_123" // TODO: Get from Firebase Auth

    val bills: Flow<List<Bill>> = billDao.getActiveBillsByUser(userId)
    val subscriptions: Flow<List<Subscription>> = subscriptionDao.getActiveSubscriptions(userId)

    fun addBill(bill: Bill) {
        viewModelScope.launch {
            billDao.insertBill(bill.copy(userId = userId))
        }
    }

    fun updateBill(bill: Bill) {
        viewModelScope.launch {
            billDao.updateBill(bill)
        }
    }

    fun deleteBill(bill: Bill) {
        viewModelScope.launch {
            billDao.deleteBill(bill)
        }
    }

    fun markBillPaid(bill: Bill) {
        viewModelScope.launch {
            billDao.updateBill(bill.copy(lastPaidDate = java.time.LocalDateTime.now()))
        }
    }
}
