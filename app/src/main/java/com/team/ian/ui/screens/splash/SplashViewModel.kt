package com.team.ian.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Role
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import com.team.ian.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
	private val authService: AuthService,
	private val alumniRepo: AlumniRepo
) : ViewModel() {
	suspend fun determineStartDestination(): Screen {
		val user = authService.getCurrentUser()

		if (user == null) {
			return Screen.Login
		}

		val alumni = alumniRepo.getAlumniByUid(user.id)

		if (alumni != null) {
			authService.updateUserRole(alumni.role)
		}

		return when {
			alumni == null -> Screen.Register
			alumni.role == Role.ADMIN -> Screen.AdminDashboard
			alumni.status == AccountStatus.PENDING -> Screen.Pending
			alumni.status == AccountStatus.REJECTED -> Screen.Rejected
			alumni.status == AccountStatus.INACTIVE -> Screen.Inactive
			alumni.status == AccountStatus.APPROVED -> Screen.Home
			else -> Screen.Login
		}
	}
}
