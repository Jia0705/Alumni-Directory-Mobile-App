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
		onSuccess: () -> Unit,
		onError: (String) -> Unit
	) {
		viewModelScope.launch {
			try {
				authService.loginWithEmail(email, password)
				onSuccess()
			} catch (e: Exception) {
				onError("Email or password is incorrect")
			}
		}
	}

	fun loginWithGoogle(
		context: android.content.Context,
		onSuccess: () -> Unit,
		onError: (String) -> Unit
	) {
		viewModelScope.launch {
			try {
				authService.signInWithGoogle(context)
				onSuccess()
			} catch (e: Exception) {
				onError(e.message ?: "Google sign-in failed")
			}
		}
	}
}
