package com.team.ian.data.api

import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {

    @POST("/send-approval")
    suspend fun sendApprovalNotification(
        @Body request: NotificationRequest
    ): NotificationResponse

    @POST("/send-rejection")
    suspend fun sendRejectionNotification(
        @Body request: NotificationRequest
    ): NotificationResponse
}

data class NotificationRequest(
    val token: String
)

data class NotificationResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null
)
