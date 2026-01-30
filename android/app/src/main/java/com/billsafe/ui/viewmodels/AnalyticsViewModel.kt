package com.billsafe.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billsafe.data.dao.BillDao
import com.billsafe.data.entities.Bill
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val billDao: BillDao
) : ViewModel() {

    private val userId = "user_123" // TODO: Get from Firebase Auth

    val bills: Flow<List<Bill>> = billDao.getAllBillsByUser(userId)
}
