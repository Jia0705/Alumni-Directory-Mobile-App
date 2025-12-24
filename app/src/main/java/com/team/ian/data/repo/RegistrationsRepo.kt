package com.team.ian.data.repo

import com.team.ian.data.model.Registration
import kotlinx.coroutines.flow.Flow

interface RegistrationsRepo{
    suspend fun getAllRegistrationsFlow(): Flow<List<Registration>>
		suspend fun getAllRegistrations(): List<Registration>
    suspend fun getRegistrationByEmail(email: String): Registration?
    suspend fun register(registration: Registration)

    companion object {
        private var instance: RegistrationsRepo? = null
        fun getInstance(): RegistrationsRepo {
            if(instance == null){
               instance = RegistrationsRepoRealImpl()
            }
            return instance!!
        }
    }
}