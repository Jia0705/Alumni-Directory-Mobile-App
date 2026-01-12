package com.team.ian.data.repo

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.ContactLinks
import com.team.ian.data.model.ExtendedInfo
import com.team.ian.data.model.Role
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlumniRepoRealImpl @Inject constructor() : AlumniRepo {
    private val dbRefAlumni = FirebaseDatabase.getInstance().getReference("users")
    private val dbRefExtended = FirebaseDatabase.getInstance().getReference("extendedInfo")
    private val dbRefContactLinks = FirebaseDatabase.getInstance().getReference("contactLinks")

    // Create alumni profile after registration
    // Called after create Firebase Auth account
    override suspend fun createAlumni(alumni: Alumni) {
        dbRefAlumni.child(alumni.uid).setValue(alumni).await()
    }

    // Get alumni profile by UID
    // Used after login (check status and role)
    override suspend fun getAlumniByUid(uid: String): Alumni? {
        return dbRefAlumni.child(uid)
            .get()
            .await()
            .getValue(Alumni::class.java)
    }

    override fun getAllUsers(): Flow<List<Alumni>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                    .mapNotNull { snapshotToAlumni(it) }
                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRefAlumni.addValueEventListener(listener)
        awaitClose { dbRefAlumni.removeEventListener(listener) }
    }

    override fun getAllAlumniExceptForPending(): Flow<List<Alumni>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                    .mapNotNull { snapshotToAlumni(it) }
                    .filter { it.status != AccountStatus.PENDING && it.role != Role.ADMIN }
                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRefAlumni.addValueEventListener(listener)
        awaitClose { dbRefAlumni.removeEventListener(listener) }
    }

    // Alumni only
    override fun getApprovedAlumni(): Flow<List<Alumni>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                    .mapNotNull { snapshotToAlumni(it) }
                    .filter { it.status == AccountStatus.APPROVED }

                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        dbRefAlumni.addValueEventListener(listener)
        awaitClose { dbRefAlumni.removeEventListener(listener) }
    }

    // Pending registrations (admin only)
    override fun getPendingAlumni(): Flow<List<Alumni>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                    .mapNotNull { snapshotToAlumni(it) }
                    .filter { it.status == AccountStatus.PENDING }

                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        dbRefAlumni.addValueEventListener(listener)
        awaitClose { dbRefAlumni.removeEventListener(listener) }
    }

    override fun getInactiveAlumni(): Flow<List<Alumni>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                    .mapNotNull { snapshotToAlumni(it) }
                    .filter { it.status == AccountStatus.INACTIVE }

                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        dbRefAlumni.addValueEventListener(listener)
        awaitClose { dbRefAlumni.removeEventListener(listener) }
    }

    override fun getRejectedAlumni(): Flow<List<Alumni>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                    .mapNotNull { snapshotToAlumni(it) }
                    .filter { it.status == AccountStatus.REJECTED }

                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        dbRefAlumni.addValueEventListener(listener)
        awaitClose { dbRefAlumni.removeEventListener(listener) }
    }

    // Edit profile
    override suspend fun updateProfile(
        uid: String,
        updates: Map<String, Any>
    ) {
        dbRefAlumni.child(uid).updateChildren(updates).await()
    }

    // Approve registration (admin only)
    // Status becomes approved and role is alumni
    override suspend fun approveAlumni(uid: String) {
        val updates = mapOf(
            "status" to AccountStatus.APPROVED.name,
            "role" to Role.ALUMNI.name,
            "updatedAt" to System.currentTimeMillis()
        )
        dbRefAlumni.child(uid).updateChildren(updates).await()
    }

    // Rejects alumni (admin only)
    override suspend fun rejectAlumni(uid: String) {
        val updates = mapOf(
            "status" to AccountStatus.REJECTED.name,
            "updatedAt" to System.currentTimeMillis()
        )
        dbRefAlumni.child(uid).updateChildren(updates).await()
    }

    // Deactivate alumni (admin only)
    override suspend fun deactivateAlumni(uid: String) {
        val updates = mapOf(
            "status" to AccountStatus.INACTIVE.name,
            "updatedAt" to System.currentTimeMillis()
        )
        dbRefAlumni.child(uid).updateChildren(updates).await()
    }

    override suspend fun addExtendedInfo(extendedInfo: ExtendedInfo) {
        dbRefExtended.child(extendedInfo.uid).setValue(extendedInfo).await()
    }

    override suspend fun getExtendedInfo(uid: String): ExtendedInfo? {
        val result = dbRefExtended.child(uid)
            .get()
            .await()
            .getValue(ExtendedInfo::class.java)
        Log.d("debugging", "extended info (${result.toString()})")
        return result
    }

    override suspend fun addContactLinks(contactLinks: ContactLinks) {
        dbRefContactLinks
            .child(contactLinks.uid)
            .setValue(contactLinks)
            .await()
    }

    override suspend fun getContactLinks(uid: String): ContactLinks? {
        val result = dbRefContactLinks.child(uid)
            .get()
            .await()
            .getValue(ContactLinks::class.java)
		Log.d("debugging", "contact links (${result.toString()})")
        return result
    }

    private fun snapshotToAlumni(snapshot: DataSnapshot): Alumni? {
        val value = snapshot.value
        if (value !is Map<*, *>) {
            return null
        }
        return try {
            snapshot.getValue(Alumni::class.java)
        } catch (e: Exception) {
            Log.d("debugging", "Skipping invalid alumni snapshot: ${e.message}")
            null
        }
    }
}
