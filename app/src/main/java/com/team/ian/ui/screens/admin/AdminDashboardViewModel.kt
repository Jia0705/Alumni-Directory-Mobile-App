package com.team.ian.ui.screens.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Alumni
import com.team.ian.data.repo.AlumniRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminDashboardViewModel(
	private val alumniRepo: AlumniRepo = AlumniRepo.getInstance(),
) : ViewModel() {
	private val _pendingAlumni = MutableStateFlow(emptyList<Alumni>())
	val pendingAlumni = _pendingAlumni.asStateFlow()

	init {
		getAllAlumni()
	}

	fun getAllAlumni() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				alumniRepo.getPendingAlumni().collect {
					_pendingAlumni.value = it
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}
}