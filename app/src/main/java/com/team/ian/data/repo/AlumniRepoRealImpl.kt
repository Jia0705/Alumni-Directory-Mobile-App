package com.team.ian.data.repo

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.ExtendedInfo
import com.team.ian.data.model.Role
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AlumniRepoRealImpl : AlumniRepo {
	private val dbRef = FirebaseDatabase.getInstance().getReference("users")
	private val dbRefExtended = FirebaseDatabase.getInstance().getReference("extendedInfo")

	// Create alumni profile after registration
	// Called after create Firebase Auth account
	override suspend fun createAlumni(alumni: Alumni) {
		dbRef.child(alumni.uid).setValue(alumni).await()
	}

	// Get alumni profile by UID
	// Used after login (check status and role)
	override suspend fun getAlumniByUid(uid: String): Alumni? {
		return dbRef.child(uid)
			.get()
			.await()
			.getValue(Alumni::class.java)
	}

	override fun getAllAlumniExceptForPending(): Flow<List<Alumni>> = callbackFlow {
		val listener = object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val list = snapshot.children
					.mapNotNull { it.getValue(Alumni::class.java) }
					.filter { it.status != AccountStatus.PENDING && it.role != Role.ADMIN }
				trySend(list)
			}

			override fun onCancelled(error: DatabaseError) {
				close(error.toException())
			}
		}
		dbRef.addValueEventListener(listener)
		awaitClose { dbRef.removeEventListener(listener) }
	}

	// Alumni only
	override fun getApprovedAlumni(): Flow<List<Alumni>> = callbackFlow {
		val listener = object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val list = snapshot.children
					.mapNotNull { it.getValue(Alumni::class.java) }
					.filter { it.status == AccountStatus.APPROVED }

				trySend(list)
			}

			override fun onCancelled(error: DatabaseError) {
				close(error.toException())
			}
		}

		dbRef.addValueEventListener(listener)
		awaitClose { dbRef.removeEventListener(listener) }
	}

	// Pending registrations (admin only)
	override fun getPendingAlumni(): Flow<List<Alumni>> = callbackFlow {
		val listener = object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val list = snapshot.children
					.mapNotNull { it.getValue(Alumni::class.java) }
					.filter { it.status == AccountStatus.PENDING }

				trySend(list)
			}

			override fun onCancelled(error: DatabaseError) {
				close(error.toException())
			}
		}

		dbRef.addValueEventListener(listener)
		awaitClose { dbRef.removeEventListener(listener) }
	}

	override fun getInactiveAlumni(): Flow<List<Alumni>> = callbackFlow {
		val listener = object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val list = snapshot.children
					.mapNotNull { it.getValue(Alumni::class.java) }
					.filter { it.status == AccountStatus.INACTIVE }

				trySend(list)
			}

			override fun onCancelled(error: DatabaseError) {
				close(error.toException())
			}
		}

		dbRef.addValueEventListener(listener)
		awaitClose { dbRef.removeEventListener(listener) }
	}

	override fun getRejectedAlumni(): Flow<List<Alumni>> = callbackFlow {
		val listener = object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val list = snapshot.children
					.mapNotNull { it.getValue(Alumni::class.java) }
					.filter { it.status == AccountStatus.INACTIVE }

				trySend(list)
			}

			override fun onCancelled(error: DatabaseError) {
				close(error.toException())
			}
		}

		dbRef.addValueEventListener(listener)
		awaitClose { dbRef.removeEventListener(listener) }
	}

	// Edit profile
	override suspend fun updateProfile(
		uid: String,
		updates: Map<String, Any>
	) {
		dbRef.child(uid).updateChildren(updates).await()
	}

	// Approve registration (admin only)
	// Status becomes approved and role is alumni
	override suspend fun approveAlumni(uid: String) {
		val updates = mapOf(
			"status" to AccountStatus.APPROVED.name,
			"role" to Role.ALUMNI.name,
			"updatedAt" to System.currentTimeMillis()
		)
		dbRef.child(uid).updateChildren(updates).await()
	}

	// Rejects alumni (admin only)
	override suspend fun rejectAlumni(uid: String) {
		val updates = mapOf(
			"status" to AccountStatus.REJECTED.name,
			"updatedAt" to System.currentTimeMillis()
		)
		dbRef.child(uid).updateChildren(updates).await()
	}

	// Deactivate alumni (admin only)
	override suspend fun deactivateAlumni(uid: String) {
		val updates = mapOf(
			"status" to AccountStatus.INACTIVE.name,
			"updatedAt" to System.currentTimeMillis()
		)
		dbRef.child(uid).updateChildren(updates).await()
	}

	override suspend fun addExtendedInfo(extendedInfo: ExtendedInfo) {
		dbRefExtended.child(extendedInfo.uid).setValue(extendedInfo).await()
	}

	// TODO: for reference, delete later
//	override suspend fun getAlumniByUid(uid: String): Alumni? {
//		return dbRef.child(uid)
//			.get()
//			.await()
//			.getValue(Alumni::class.java)
//	}

	override suspend fun getExtendedInfo(uid: String): ExtendedInfo? {
		val result = dbRefExtended.child(uid)
			.get()
			.await()
			.getValue(ExtendedInfo::class.java)
//		Log.d("debugging", "getExtendedInfo for uid=$uid: $result")
		return result
	}

	// TODO: probably remove
	override suspend fun uploadProfilePhoto(uid: String, imageUri: Uri) {
		Log.d("debugging", "uploadProfilePhoto trigger?")
//		val fileName = "profile_${uid}_${UUID.randomUUID()}.jpg"
//		val storageRef = dbRefExtended.ref
//		val imageRef = storageRef.child("profile_photos/$uid/$fileName")

//		imageRef.putFile
	}
}

