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

class AdminManageAlumniViewModel(
	private val alumniRepo: AlumniRepo = AlumniRepo.getInstance()
): ViewModel(){
	private val _alumni = MutableStateFlow(emptyList<Alumni>())
	val alumni = _alumni.asStateFlow()

	init {
		getAllUsers()
	}

	fun getAllUsers(){
		viewModelScope.launch (Dispatchers.IO) {
			try{
				alumniRepo.getAllAlumniExceptForPending().collect {
					_alumni.value = it
				}
			}catch (e: Exception){
				Log.d("debugging",e.toString())
			}
		}
	}
}