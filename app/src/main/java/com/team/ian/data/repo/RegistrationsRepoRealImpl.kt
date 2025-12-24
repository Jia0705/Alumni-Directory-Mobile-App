package com.team.ian.data.repo

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.team.ian.data.model.Registration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RegistrationsRepoRealImpl : RegistrationsRepo {
	private val dbRef = FirebaseDatabase.getInstance().getReference("registrations")

	override suspend fun getAllRegistrationsFlow() = callbackFlow {
		val listener = object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val registrations = mutableListOf<Registration>()
				for (registrationSnapshot in snapshot.children) {
					registrationSnapshot.getValue(Registration::class.java)?.let {
						registrations.add(it.copy(registrationSnapshot.key!!))
					}
				}
				trySend(registrations)
			}

			override fun onCancelled(error: DatabaseError) {
				throw error.toException()
			}
		}
		dbRef.addValueEventListener(listener)
		awaitClose()
	}

	override suspend fun getAllRegistrations(): List<Registration> {
		TODO("Not yet implemented")
	}

	override suspend fun getRegistrationByEmail(email: String): Registration? {
		val registration = dbRef.child(email).get().await()
		return registration.key?.let {
			registration.getValue(Registration::class.java)?.copy(it)
		}
	}

	override suspend fun register(registration: Registration) {
		if (getRegistrationByEmail(registration.email) != null) {
			dbRef.push().setValue(registration).await()
		}
	}
}