package com.billsafe.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billsafe.auth.UserSession
import com.billsafe.data.dao.BillDao
import com.billsafe.data.dao.SubscriptionDao
import com.billsafe.data.entities.Bill
import com.billsafe.data.entities.Subscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel @Inject constructor(
    private val billDao: BillDao,
    private val subscriptionDao: SubscriptionDao,
    private val userSession: UserSession
) : ViewModel() {

    val bills: Flow<List<Bill>> = userSession.uid.flatMapLatest { userId ->
        if (userId.isNullOrBlank()) {
            flowOf(emptyList())
        } else {
            billDao.getActiveBillsByUser(userId)
        }
    }

    val subscriptions: Flow<List<Subscription>> = userSession.uid.flatMapLatest { userId ->
        if (userId.isNullOrBlank()) {
            flowOf(emptyList())
        } else {
            subscriptionDao.getActiveSubscriptions(userId)
        }
    }

    fun addBill(bill: Bill) {
        viewModelScope.launch {
            val userId = userSession.uid.value ?: return@launch
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
