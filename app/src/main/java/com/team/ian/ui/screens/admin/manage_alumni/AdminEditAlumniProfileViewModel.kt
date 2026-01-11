package com.team.ian.ui.screens.admin.manage_alumni

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.AlumniField
import com.team.ian.data.model.ContactLinks
import com.team.ian.data.model.ExtendedInfo
import com.team.ian.data.model.Role
import com.team.ian.data.repo.AlumniRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminEditAlumniProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val alumniRepo: AlumniRepo
) : ViewModel() {
    private val alumniId = savedStateHandle.get<String>("alumniId")!!
    private val _alumni = MutableStateFlow(Alumni())
    val alumni = _alumni.asStateFlow()

    private val _extendedInfo = MutableStateFlow(ExtendedInfo())
    val extendedInfo = _extendedInfo.asStateFlow()

    private val _contactLinks = MutableStateFlow(ContactLinks())
    val contactLinks = _contactLinks.asStateFlow()

    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    init {
        getAlumniById()
        getAlumniExtendedInfo()
        getAlumniContactLinks()
    }

    fun getAlumniById() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                alumniRepo.getAlumniByUid(alumniId)?.let {
                    _alumni.value = it
                }
            } catch (e: Exception) {
                Log.d("debugging", e.toString())
            }
        }
    }

    fun updateAlumniField(field: AlumniField, value: String) {
        val current = _alumni.value
        val updated = when (field) {
            // basic info
            AlumniField.FULL_NAME -> current.copy(fullName = value)
            AlumniField.EMAIL -> current.copy(email = value)
            AlumniField.GRAD_YEAR -> current.copy(graduationYear = value.toInt())
            AlumniField.DEPARTMENT -> current.copy(department = value)

            // professional info
            AlumniField.JOB_TITLE -> current.copy(jobTitle = value)
            AlumniField.COMPANY -> current.copy(company = value)
            AlumniField.TECH_STACK -> current.copy(primaryStack = value)

            // locational info
            AlumniField.CITY -> current.copy(city = value)
            AlumniField.COUNTRY -> current.copy(country = value)
            else -> current
        }
        _alumni.value = updated
    }

    fun updateAlumniStatusState(updatedStatus: AccountStatus) {
        val current = _alumni.value
        _alumni.value = current.copy(status = updatedStatus)
    }

    fun resetAllStates() {
        getAlumniById()
    }
	fun updateAlumniRole(updatedRole: Role) {
		val current = _alumni.value
		_alumni.value = current.copy(role = updatedRole)
	}


    fun finishEditing() {
        val updatedAlumni = _alumni.value

        val fieldsToCheck = listOf(
            updatedAlumni.fullName,
            updatedAlumni.email,
            updatedAlumni.department,
            updatedAlumni.jobTitle,
            updatedAlumni.company,
            updatedAlumni.primaryStack,
            updatedAlumni.city,
            updatedAlumni.country,
        )

        for (field in fieldsToCheck) {
            require(field.isNotBlank()) {
                "$field cannot be empty"
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updates = mapOf<String, Any>(
                    "fullName" to updatedAlumni.fullName,
                    "email" to updatedAlumni.email,
                    "graduationYear" to updatedAlumni.graduationYear,
                    "department" to updatedAlumni.department,
                    "jobTitle" to updatedAlumni.jobTitle,
                    "company" to updatedAlumni.company,
                    "primaryStack" to updatedAlumni.primaryStack,
                    "city" to updatedAlumni.city,
                    "country" to updatedAlumni.country,
                    "status" to updatedAlumni.status,
                    "role" to updatedAlumni.role
                )
                alumniRepo.updateProfile(alumniId, updates)
                _finish.emit(Unit)
            } catch (e: Exception) {
                Log.d("debugging", e.toString())
            }
        }
    }

    fun getAlumniExtendedInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                alumniRepo.getExtendedInfo(alumniId)?.let {
                    _extendedInfo.value = it
                    Log.d("debugging",_extendedInfo.value.toString())
                }
            }catch (e: Exception){
                Log.d("debugging",e.toString())
            }
        }
    }

    fun getAlumniContactLinks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                alumniRepo.getContactLinks(alumniId)?.let {
                    _contactLinks.value = it
                    Log.d("debugging",_contactLinks.value.toString())
                }
            }catch (e: Exception){
                Log.d("debugging",e.toString())
            }
        }
    }
}