package com.billsafe.data.api

import com.billsafe.data.entities.Bill
import com.billsafe.data.entities.Subscription
import com.billsafe.data.api.dto.BackendUserDto
import com.billsafe.data.api.dto.CreateUserRequest
import retrofit2.http.*

interface BillSafeApi {
    
    // User endpoints
    @POST("/api/users/{uid}")
    suspend fun createUser(@Path("uid") uid: String, @Body request: CreateUserRequest): ApiResponse<BackendUserDto>

    @GET("/api/users/{uid}")
    suspend fun getUser(@Path("uid") uid: String): ApiResponse<BackendUserDto>

    @GET("/api/users/{uid}/stats")
    suspend fun getUserStats(@Path("uid") uid: String): ApiResponse<Map<String, Any>>

    // Bill endpoints
    @POST("/api/bills/{uid}/create")
    suspend fun createBill(@Path("uid") uid: String, @Body bill: Bill): ApiResponse<Bill>

    @GET("/api/bills/{uid}/active")
    suspend fun getActiveBills(@Path("uid") uid: String): ApiResponse<List<Bill>>

    @GET("/api/bills/{uid}/all")
    suspend fun getAllBills(@Path("uid") uid: String): ApiResponse<List<Bill>>

    @PUT("/api/bills/{billId}")
    suspend fun updateBill(@Path("billId") billId: String, @Body bill: Bill): ApiResponse<Bill>

    @DELETE("/api/bills/{billId}")
    suspend fun deleteBill(@Path("billId") billId: String): ApiResponse<Map<String, String>>

    @PUT("/api/bills/{billId}/mark-paid")
    suspend fun markBillPaid(@Path("billId") billId: String): ApiResponse<Bill>

    // Subscription endpoints
    @POST("/api/subscriptions/{uid}/create")
    suspend fun createSubscription(@Path("uid") uid: String, @Body subscription: Subscription): ApiResponse<Subscription>

    @GET("/api/subscriptions/{uid}/active")
    suspend fun getActiveSubscriptions(@Path("uid") uid: String): ApiResponse<List<Subscription>>

    @GET("/api/subscriptions/{uid}/unused")
    suspend fun getUnusedSubscriptions(@Path("uid") uid: String): ApiResponse<List<Subscription>>

    @PUT("/api/subscriptions/{subscriptionId}")
    suspend fun updateSubscription(@Path("subscriptionId") subscriptionId: String, @Body subscription: Subscription): ApiResponse<Subscription>

    @PUT("/api/subscriptions/{subscriptionId}/cancel")
    suspend fun cancelSubscription(@Path("subscriptionId") subscriptionId: String): ApiResponse<Subscription>
}

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null
)
