package com.team.ian.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.team.ian.data.model.Alumni
import com.team.ian.data.repo.AlumniRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminViewRegistrationViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val alumniRepo: AlumniRepo
) : ViewModel() {
	private val pendingAlumniId = savedStateHandle.get<String>("pendingAlumniId")!!
	private val _pendingAlumni = MutableStateFlow(Alumni())
	val pendingAlumni = _pendingAlumni.asStateFlow()

	init {
		getPendingAlumniById(pendingAlumniId)
	}

	fun getPendingAlumniById(id: String) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				alumniRepo.getAlumniByUid(id)?.let {
					_pendingAlumni.value = it
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}

	fun approveAlumni() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				alumniRepo.approveAlumni(pendingAlumniId)
				Log.d("debugging", "Alumni approved")
			} catch (e: Exception) {
				Log.e("debugging", "Error: ${e.message}")
			}
		}
	}

	fun rejectAlumni() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				alumniRepo.rejectAlumni(pendingAlumniId)
				Log.d("debugging", "Alumni rejected")
			} catch (e: Exception) {
				Log.e("debugging", "Error: ${e.message}")
			}
		}
	}
}