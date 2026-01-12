package com.team.ian.ui.screens.status

import androidx.lifecycle.ViewModel
import com.team.ian.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
	private val authService: AuthService
) : ViewModel() {
	fun signOut() {
		authService.signOut()
	}
}
