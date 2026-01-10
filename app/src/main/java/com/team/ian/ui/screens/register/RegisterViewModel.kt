package com.team.ian.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.Registration
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import kotlinx.coroutines.launch

class RegisterViewModel(
	private val authService: AuthService = AuthService.getInstance(),
	private val alumniRepo: AlumniRepo = AlumniRepo.getInstance()
) : ViewModel() {

	fun register(
		registration: Registration,
		password: String,
		onSuccess: () -> Unit,
		onError: (String) -> Unit
	) {
		viewModelScope.launch {
			try {
				// Create Firebase Auth account
				authService.registerWithEmail(
					email = registration.email,
					password = password
				)

				val user = authService.getCurrentUser()
					?: throw IllegalStateException("Authentication failed")

				// Convert Registration to Alumni
				val alumni = Alumni(
					uid = user.id,
					fullName = registration.name,
					email = registration.email,
					graduationYear = registration.gradYear,
					department = registration.department,
					jobTitle = registration.position,
					company = registration.organization,
					primaryStack = registration.techStack,
					city = registration.city,
					country = registration.country,
					linkedin = registration.linkedin,
					github = registration.github,
					phone = registration.phone,
					shortBio = registration.shortBio.ifBlank { registration.shortBio },
					skills = registration.skills.split(",")
						.map { it.trim() }
						.filter { it.isNotBlank() },
					pastJobHistory = registration.workExperience.split(",")
						.map { it.trim() }
						.filter { it.isNotBlank() },
				)

				// Save alumni profile (status is PENDING and role is NONE)
				alumniRepo.createAlumni(alumni)
				onSuccess()
			} catch (e: Exception) {
				onError(e.message ?: "Registration failed")
			}
		}
	}
}