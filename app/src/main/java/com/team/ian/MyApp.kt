package com.team.ian

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyApp: Application() {
	override fun onCreate() {
		super.onCreate()

		// Get FCM token and save to Realtime Database
		FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
			if (!task.isSuccessful) {
				Log.w("token", "Fetching token failed", task.exception)
				return@addOnCompleteListener
			}
			val token = task.result
			Log.d("token", "Token: $token")

			// Save token to Realtime Database for this user
			CoroutineScope(Dispatchers.IO).launch {
				try {
					val currentUser = AuthService.getInstance().getCurrentUser()
					if (currentUser != null) {
						val updates = mapOf("token" to token)
						AlumniRepo.getInstance().updateProfile(currentUser.id, updates)
						Log.d("token", "Token saved to database")
					}
				} catch (e: Exception) {
					Log.e("token", "Failed to save token", e)
				}
			}
		}
	}
}