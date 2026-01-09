package com.team.ian.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.ExtendedInfo
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddOrEditExtendedInfoViewModel(
	private val alumniRepo: AlumniRepo = AlumniRepo.getInstance(),
	private val authService: AuthService = AuthService.getInstance()
) : ViewModel() {
	private val _extendedInfo = MutableStateFlow<ExtendedInfo?>(null)
	val extendedInfo = _extendedInfo.asStateFlow()
	private val _finish = MutableSharedFlow<Unit>()
	val finish = _finish.asSharedFlow()

	init {
		getExtendedInfo()
	}

	fun getExtendedInfo() {
		viewModelScope.launch(Dispatchers.IO) {
			try{
				val userId = authService.getCurrentUser()?.id
				userId?.let {
					alumniRepo.getExtendedInfo(userId)?.let { extendedInfo ->
						_extendedInfo.value = extendedInfo
					}
				}
			}catch (e: Exception){
				Log.d("debugging",e.toString())
			}
		}
	}

	fun addExtendedInfoToAlumni(
		pastJobHistory: List<String>,
		skills: List<String>,
		shortBio: String
	) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val userId = authService.getCurrentUser()?.id
				userId?.let {
					alumniRepo.addExtendedInfo(
						ExtendedInfo(
							uid = userId,
							pastJobHistory = pastJobHistory,
							skills = skills,
							shortBio = shortBio
						)
					)
					_finish.emit(Unit)
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}
}