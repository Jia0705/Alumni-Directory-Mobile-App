package com.team.ian.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.ExtendedInfo
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExtendedInfoViewModel(
	private val alumniRepo: AlumniRepo = AlumniRepo.getInstance(),
	private val authService: AuthService = AuthService.getInstance()
): ViewModel() {
	private val _extendedInfo = MutableStateFlow(ExtendedInfo())
	val extendedInfo = _extendedInfo.asStateFlow()

	init {
		getExtendedInfo()
	}

	fun getExtendedInfo(){
		Log.d("debugging", "getExtendedInfo trigger")
		viewModelScope.launch(Dispatchers.IO) {
			try{
				val userId = authService.getCurrentUser()?.id
				if (userId == null) {
					Log.e("debugging", "getExtendedInfo: userId is null - user not logged in?")
					return@launch
				}
				
				Log.d("debugging", "getExtendedInfo: fetching for userId=$userId")
				val result = alumniRepo.getExtendedInfo(userId)
				
				if (result != null) {
					Log.d("debugging", "getExtendedInfo: SUCCESS - $result")
					_extendedInfo.value = result
				} else {
					Log.e("debugging", "getExtendedInfo: FAILED - returned null for userId=$userId")
				}
			}catch (e: Exception){
				Log.e("debugging", "getExtendedInfo: EXCEPTION - ${e.message}", e)
			}
		}
	}
}