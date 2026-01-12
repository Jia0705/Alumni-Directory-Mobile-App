package com.team.ian.data.repo

import com.team.ian.data.model.Alumni
import com.team.ian.data.model.ContactLinks
import com.team.ian.data.model.ExtendedInfo
import kotlinx.coroutines.flow.Flow

interface AlumniRepo {
	suspend fun createAlumni(alumni: Alumni)
	suspend fun getAlumniByUid(uid: String): Alumni?
	fun getAllUsers(): Flow<List<Alumni>>
	fun getAllAlumniExceptForPending(): Flow<List<Alumni>>
	fun getApprovedAlumni(): Flow<List<Alumni>>
	fun getPendingAlumni(): Flow<List<Alumni>>
	fun getInactiveAlumni(): Flow<List<Alumni>>
	fun getRejectedAlumni(): Flow<List<Alumni>>
	suspend fun updateProfile(uid: String, updates: Map<String, Any>)
	suspend fun approveAlumni(uid: String)
	suspend fun rejectAlumni(uid: String)
	suspend fun deactivateAlumni(uid: String)
	suspend fun addExtendedInfo(extendedInfo: ExtendedInfo)
	suspend fun getExtendedInfo(uid: String): ExtendedInfo?
    suspend fun addContactLinks(contactLinks: ContactLinks)
    suspend fun getContactLinks(uid:String): ContactLinks?
}
