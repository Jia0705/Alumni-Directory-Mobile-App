package com.team.ian.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.ContactLinks
import com.team.ian.data.model.ExtendedInfo
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
	private val alumniRepo: AlumniRepo = AlumniRepo.getInstance(),
    private val authService: AuthService = AuthService.getInstance()
) : ViewModel() {
	private val _alumni = MutableStateFlow(Alumni())
	val alumni = _alumni.asStateFlow()
    private val _extendedInfo = MutableStateFlow(ExtendedInfo())
    val extendedInfo = _extendedInfo.asStateFlow()
    private val _contactLinks = MutableStateFlow(ContactLinks())
    val contactLinks = _contactLinks.asStateFlow()
    val userId = authService.getCurrentUser()?.id

    init {
        loadAlumniProfile()
        getContactLinks()
        getExtendedInfo()
    }


	fun loadAlumniProfile() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
                userId?.let {
                    alumniRepo.getAlumniByUid(userId)?.let {
                        _alumni.value = it
                    }
                }
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}

    fun getExtendedInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userId?.let {
                    alumniRepo.getExtendedInfo(userId)?.let {
                        _extendedInfo.value = it
                    }
                }
            }catch (e: Exception){
                Log.d("debugging",e.toString())
            }
        }
    }

    fun getContactLinks(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userId?.let {
                    alumniRepo.getContactLinks(userId)?.let {
                        _contactLinks.value = it
                    }
                }
            }catch (e: Exception){
                Log.d("debugging",e.toString())
            }
        }
    }

    fun updateAvatarColor(color: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userId?.let {
                    alumniRepo.updateProfile(it, mapOf("avatarColor" to color))
                    _alumni.value = _alumni.value.copy(avatarColor = color)
                }
            } catch (e: Exception) {
                Log.d("debugging", e.toString())
            }
        }
    }
}
