package com.team.ian.ui.navigation

import androidx.lifecycle.ViewModel
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppNavViewModel @Inject constructor(
	private val authService: AuthService,
	private val alumniRepo: AlumniRepo
) : ViewModel() {
	val user = authService.user
	val pendingAlumni = alumniRepo.getPendingAlumni()
}
