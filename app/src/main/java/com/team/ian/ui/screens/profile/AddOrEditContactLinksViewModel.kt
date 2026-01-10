package com.team.ian.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.ContactLinks
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddOrEditContactLinksViewModel(
    private val alumniRepo: AlumniRepo = AlumniRepo.getInstance(),
    private val authService: AuthService = AuthService.getInstance()
) : ViewModel() {
    private val _contactLinks = MutableStateFlow<ContactLinks?>(null)
    val contactLinks = _contactLinks.asStateFlow()
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    init {
        getContactLinks()
    }

    fun addContactInfoToAlumni(
        contactLinks: ContactLinks
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = authService.getCurrentUser()?.id
                userId?.let {
                    alumniRepo.addContactLinks(
                        ContactLinks(
                            uid = userId,
                            linkedIn = contactLinks.linkedIn,
                            github = contactLinks.github,
                            phoneNumber = contactLinks.phoneNumber
                        )
                    )
                    _finish.emit(Unit)
                }
            } catch (e: Exception) {
                Log.d("debugging", e.toString())
            }
        }
    }

    fun getContactLinks(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = authService.getCurrentUser()?.id
                userId?.let {
                    alumniRepo.getContactLinks(userId)?.let { contactLinks ->
                        _contactLinks.value = contactLinks
                    }
                }
            }catch (e: Exception){
                Log.d("debugging", e.toString())
            }
        }
    }
}