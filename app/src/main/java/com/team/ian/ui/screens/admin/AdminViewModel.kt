package com.team.ian.ui.screens.admin

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Registration
import com.team.ian.data.repo.RegistrationsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
	private val registrationsRepo: RegistrationsRepo = RegistrationsRepo.getInstance()
): ViewModel() {
	private val _registrations = MutableStateFlow(emptyList<Registration>())
	val registrations = _registrations.asStateFlow()

	fun getAllRegistrations() {
		viewModelScope.launch(Dispatchers.IO) {
			try{
				registrationsRepo.getAllRegistrationsFlow().collect {
					_registrations.value = it
				}
			}catch (e: Exception){
				Log.d("debugging", e.toString())
			}
		}
	}
}