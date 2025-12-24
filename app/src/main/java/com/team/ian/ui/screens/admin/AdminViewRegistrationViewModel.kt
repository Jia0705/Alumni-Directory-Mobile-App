package com.team.ian.ui.screens.admin

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Registration
import com.team.ian.data.repo.RegistrationsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewRegistrationViewModel(
	private val registrationsRepo: RegistrationsRepo = RegistrationsRepo.getInstance(),
	private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
	private val registrationId = savedStateHandle.get<String>("registrationId")!!
	private val _registration = MutableStateFlow(Registration())
	val registration = _registration.asStateFlow()

	init {
		getRegistrationById(registrationId)
	}

	fun getRegistrationById(id: String) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				registrationsRepo.getRegistrationById(id)?.let {
					_registration.value = it
					Log.d("debugging", _registration.value.toString())
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}

}