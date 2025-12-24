package com.team.ian.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.service.AuthService
import kotlinx.coroutines.launch

class LoginViewModel(
	private val authService: AuthService = AuthService.getInstance()
) : ViewModel() {

	fun loginWithEmail(
		email: String,
		password: String,
		onError: (String) -> Unit
	) {
		viewModelScope.launch {
			try {
				authService.loginWithEmail(email, password)
			} catch (e: Exception) {
				onError(e.message ?: "Login failed")
			}
		}
	}

	fun loginWithGoogle(
		context: android.content.Context,
		onError: (String) -> Unit
	) {
		viewModelScope.launch {
			try {
				authService.signInWithGoogle(context)
			} catch (e: Exception) {
				onError(e.message ?: "Google sign-in failed")
			}
		}
	}
}
