package com.team.ian.ui.screens.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Registration
import com.team.ian.data.repo.RegistrationsRepo
import kotlinx.coroutines.launch

class RegisterViewModel(
   private val repo: RegistrationsRepo = RegistrationsRepo.getInstance()
) : ViewModel() {

    fun register(registration: Registration) {
        try {
            viewModelScope.launch {
                repo.register(registration)
            }
        } catch (e: Exception) {
            Log.d("debugging", e.toString())
        }
    }
}