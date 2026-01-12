package com.team.ian.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val authService: AuthService
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
				val signedIn = authService.signInWithGoogle(context)
				if (signedIn) {
					onSuccess()
				} else {
					onError("Google sign-in was cancelled")
				}
			} catch (e: Exception) {
				onError(e.message ?: "Google sign-in failed")
			}
		}
	}
}
