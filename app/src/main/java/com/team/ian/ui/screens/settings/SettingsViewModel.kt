package com.team.ian.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Alumni
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val alumniRepo: AlumniRepo,
	private val authService: AuthService
) : ViewModel() {
	private val _alumni = MutableStateFlow(Alumni())
	val alumni = _alumni.asStateFlow()

	init {
		loadProfile()
	}

	private fun loadProfile() {
		viewModelScope.launch(Dispatchers.IO) {
			authService.getCurrentUser()?.id?.let { userId ->
				alumniRepo.getAlumniByUid(userId)?.let {
					_alumni.value = it
				}
			}
		}
	}

	fun updatePrivacyControls(
		showEmail: Boolean,
		showPhone: Boolean,
		showLinkedIn: Boolean,
		showGithub: Boolean
	) {
		viewModelScope.launch(Dispatchers.IO) {
			authService.getCurrentUser()?.id?.let { userId ->
				val updates = mapOf(
					"showEmail" to showEmail,
					"showPhone" to showPhone,
					"showLinkedIn" to showLinkedIn,
					"showGithub" to showGithub
				)
				alumniRepo.updateProfile(userId, updates)
			}
		}
	}
}