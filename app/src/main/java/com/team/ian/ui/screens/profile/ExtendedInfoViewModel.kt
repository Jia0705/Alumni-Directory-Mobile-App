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

	fun getExtendedInfo(){
		viewModelScope.launch(Dispatchers.IO) {
			try{
				val userId = authService.getCurrentUser()?.id
				userId?.let {
					alumniRepo.getExtendedInfo(userId)?.let {
						Log.d("debugging", it.toString())
						_extendedInfo.value = it
					}
				}
			}catch (e: Exception){
				Log.d("debugging", e.toString())
			}
		}
	}
}