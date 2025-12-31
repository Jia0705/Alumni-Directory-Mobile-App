package com.team.ian.data.repo

import com.team.ian.data.model.Alumni
import kotlinx.coroutines.flow.Flow

interface AlumniRepo {
	suspend fun createAlumni(alumni: Alumni)
	suspend fun getAlumniByUid(uid: String): Alumni?
	fun getAllAlumniExceptForPending(): Flow<List<Alumni>>
	fun getApprovedAlumni(): Flow<List<Alumni>>
	fun getPendingAlumni(): Flow<List<Alumni>>
	fun getInactiveAlumni(): Flow<List<Alumni>>
	fun getRejectedAlumni(): Flow<List<Alumni>>
	suspend fun updateProfile(uid: String, updates: Map<String, Any>)
	suspend fun approveAlumni(uid: String)
	suspend fun rejectAlumni(uid: String)
	suspend fun deactivateAlumni(uid: String)

	companion object {
		private var instance: AlumniRepo? = null
		fun getInstance(): AlumniRepo {
			if (instance == null) {
				instance = AlumniRepoRealImpl()
			}
			return instance!!
		}
	}
}
