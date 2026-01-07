package com.team.ian.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.ExtendedInfo
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddExtendedInformationViewModel(
	private val alumniRepo: AlumniRepo = AlumniRepo.getInstance(),
	private val authService: AuthService = AuthService.getInstance()
): ViewModel() {
	private val _alumni = MutableStateFlow(Alumni())
	val alumni = _alumni.asStateFlow()

	private val _finish = MutableSharedFlow<Unit>()
	val finish = _finish.asSharedFlow()

	init {
		loadExtendedInfo()
	}

	private fun addExtendedInfoToAlumni(extendedInfo: ExtendedInfo) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val userId = authService.getCurrentUser()?.id
				userId?.let {
					alumniRepo.addExtendedInfo(userId, extendedInfo)
					_finish.emit(Unit)
				}
			}catch (e: Exception){
				Log.d("debugging", e.toString())
			}
		}
	}

	private fun loadExtendedInfo(){
		viewModelScope.launch(Dispatchers.IO){
			try {
				val userId = authService.getCurrentUser()?.id
				userId?.let {

				}
			}catch (e: Exception){
				Log.d("debugging", e.toString())
			}
		}
	}
}