package com.team.ian.ui.screens.admin.registrations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.team.ian.data.model.Alumni
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.data.api.NotificationService
import com.team.ian.data.api.NotificationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminViewRegistrationViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val alumniRepo: AlumniRepo,
	private val notificationService: NotificationService
) : ViewModel() {
	private val pendingAlumniId = savedStateHandle.get<String>("pendingAlumniId")!!
	private val _pendingAlumni = MutableStateFlow(Alumni())
	val pendingAlumni = _pendingAlumni.asStateFlow()

	init {
		getPendingAlumniById(pendingAlumniId)
	}

	fun getPendingAlumniById(id: String) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				alumniRepo.getAlumniByUid(id)?.let {
					_pendingAlumni.value = it
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}

	fun approveAlumni() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				alumniRepo.approveAlumni(pendingAlumniId)
				Log.d("debugging", "Alumni approved in database")
				
				val userToken = _pendingAlumni.value.token
				if (userToken.isNotBlank()) {
					try {
						val response = notificationService.sendApprovalNotification(
							NotificationRequest(token = userToken)
						)
						if (response.success) {
							Log.d("debugging", "Approval notification sent")
						} else {
							Log.e("debugging", "Notification failed: ${response.error}")
						}
					} catch (e: Exception) {
						Log.e("debugging", "Failed to send notification: ${e.message}")
					}
				} else {
					Log.w("debugging", "User has no FCM token, cannot send notification")
				}
			} catch (e: Exception) {
				Log.e("debugging", "Error approving alumni: ${e.message}")
			}
		}
	}

	fun rejectAlumni() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				alumniRepo.rejectAlumni(pendingAlumniId)
				Log.d("debugging", "Alumni rejected in database")
				
				val userToken = _pendingAlumni.value.token
				if (userToken.isNotBlank()) {
					try {
						val response = notificationService.sendRejectionNotification(
							NotificationRequest(token = userToken)
						)
						if (response.success) {
							Log.d("debugging", "Rejection notification sent")
						} else {
							Log.e("debugging", "Notification failed: ${response.error}")
						}
					} catch (e: Exception) {
						Log.e("debugging", "Failed to send notification: ${e.message}")
					}
				} else {
					Log.w("debugging", "User has no FCM token, cannot send notification")
				}
			} catch (e: Exception) {
				Log.e("debugging", "Error rejecting alumni: ${e.message}")
			}
		}
	}
}