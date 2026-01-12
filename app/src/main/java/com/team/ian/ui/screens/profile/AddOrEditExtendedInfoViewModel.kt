package com.team.ian.ui.screens.profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.ExtendedInfo
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
class AddOrEditExtendedInfoViewModel @Inject constructor(
    private val alumniRepo: AlumniRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val alumniId = savedStateHandle.get<String>("alumniId")!!
    private val _extendedInfo = MutableStateFlow<ExtendedInfo?>(null)
    val extendedInfo = _extendedInfo.asStateFlow()
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    init {
        getExtendedInfo()
    }

    fun getExtendedInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val info = alumniRepo.getExtendedInfo(alumniId)
                if (info != null) {
                    _extendedInfo.value = info
                } else {
                    val alumni = alumniRepo.getAlumniByUid(alumniId)
                    if (alumni != null) {
                        _extendedInfo.value = ExtendedInfo(
                            uid = alumniId,
                            pastJobHistory = alumni.pastJobHistory,
                            skills = alumni.skills,
                            shortBio = alumni.shortBio
                        )
                    }
                }

            } catch (e: Exception) {
                Log.d("debugging", e.toString())
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
                alumniRepo.addExtendedInfo(
                    ExtendedInfo(
                        uid = alumniId,
                        pastJobHistory = pastJobHistory,
                        skills = skills,
                        shortBio = shortBio
                    )
                )
                _finish.emit(Unit)

            } catch (e: Exception) {
                Log.d("debugging", e.toString())
            }
        }
    }
}