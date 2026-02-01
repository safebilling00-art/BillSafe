package com.billsafe.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.billsafe.auth.UserSession
import com.billsafe.data.dao.BillDao
import com.billsafe.data.entities.Bill
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AnalyticsViewModel @Inject constructor(
    private val billDao: BillDao,
    private val userSession: UserSession
) : ViewModel() {

    val bills: Flow<List<Bill>> = userSession.uid.flatMapLatest { userId ->
        if (userId.isNullOrBlank()) {
            flowOf(emptyList())
        } else {
            billDao.getAllBillsByUser(userId)
        }
    }
}
