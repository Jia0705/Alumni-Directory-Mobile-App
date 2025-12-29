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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class AdminViewPendingAlumniViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val alumniRepo: AlumniRepo
) : ViewModel() {
	private val pendingAlumniId = savedStateHandle.get<String>("pendingAlumniId")!!
	private val _pendingAlumni = MutableStateFlow<Alumni?>(null)
	private val _finish = MutableSharedFlow<Unit>()
	val finish = _finish.asSharedFlow()
	val pendingAlumni = _pendingAlumni.asStateFlow()

	init {
		getPendingAlumniById(pendingAlumniId)
	}

	fun getPendingAlumniById(uid: String) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				alumniRepo.getAlumniByUid(uid)?.let {
					_pendingAlumni.value = it
					Log.d("debugging",pendingAlumni.value.toString())
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}

	fun approveAlumni(uid: String) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				Log.d("debugging", "starting to approve")
				alumniRepo.approveAlumni(uid)
				Log.d("debugging", "approved")
				_finish.emit(Unit)
			}catch (e: Exception){
				Log.d("debugging", e.toString())
			}
		}
	}
}