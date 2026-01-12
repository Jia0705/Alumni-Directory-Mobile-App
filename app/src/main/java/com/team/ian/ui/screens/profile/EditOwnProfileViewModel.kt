package com.team.ian.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.AlumniField
import com.team.ian.data.model.ExtendedInfo
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditOwnProfileViewModel @Inject constructor(
	private val alumniRepo: AlumniRepo,
	private val authService: AuthService
) : ViewModel() {
	private val _alumni = MutableStateFlow(Alumni())
	val alumni = _alumni.asStateFlow()

	private val _extendedInfo = MutableStateFlow(ExtendedInfo())
	val extendedInfo = _extendedInfo.asStateFlow()

	private val _finish = MutableSharedFlow<Unit>()
	val finish = _finish.asSharedFlow()

	init {
		loadProfile()
		getExtendedInfo()
	}

    fun loadProfile() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val userId = authService.getCurrentUser()?.id
				userId?.let {
					alumniRepo.getAlumniByUid(it)?.let { alumniData ->
						_alumni.value = alumniData
					}
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}

	fun updateAlumniField(field: AlumniField, value: String) {
		val current = _alumni.value
		val updated = when (field) {
			AlumniField.JOB_TITLE -> current.copy(jobTitle = value)
			AlumniField.COMPANY -> current.copy(company = value)
			AlumniField.TECH_STACK -> current.copy(primaryStack = value)
			AlumniField.CITY -> current.copy(city = value)
			AlumniField.COUNTRY -> current.copy(country = value)
			AlumniField.LINKEDIN -> current.copy(linkedin = value)
			AlumniField.GITHUB -> current.copy(github = value)
			AlumniField.PHONE -> current.copy(phone = value)
			else -> current   // Not allow to edit other fields
		}
		_alumni.value = updated
	}

	fun resetAllStates() {
		loadProfile()
	}

	fun finishEditing() {
		val updatedAlumni = _alumni.value
		val fieldsToCheck = listOf(
			updatedAlumni.jobTitle,
			updatedAlumni.company,
			updatedAlumni.primaryStack,
			updatedAlumni.city,
			updatedAlumni.country,
		)
		for (field in fieldsToCheck) {
			require(field.isNotBlank()) {
				"Required fields cannot be empty"
			}
		}
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val userId = authService.getCurrentUser()?.id ?: return@launch
				val updates = mapOf<String, Any>(
					"jobTitle" to updatedAlumni.jobTitle,
					"company" to updatedAlumni.company,
					"primaryStack" to updatedAlumni.primaryStack,
					"city" to updatedAlumni.city,
					"country" to updatedAlumni.country,
					"linkedin" to updatedAlumni.linkedin,
					"github" to updatedAlumni.github,
					"phone" to updatedAlumni.phone
				)
				alumniRepo.updateProfile(userId, updates)
				_finish.emit(Unit)
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}

	fun getExtendedInfo() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val userId = authService.getCurrentUser()?.id
				userId?.let {
					alumniRepo.getExtendedInfo(it)?.let { info ->
						_extendedInfo.value = info
					}
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}
}