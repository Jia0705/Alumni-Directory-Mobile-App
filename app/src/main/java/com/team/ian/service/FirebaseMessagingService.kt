package com.team.ian.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.team.ian.MainActivity
import com.team.ian.R
import com.team.ian.data.repo.AlumniRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IanFirebaseMessagingService: FirebaseMessagingService() {
	@Inject lateinit var authService: AuthService
	@Inject lateinit var alumniRepo: AlumniRepo

	override fun onNewToken(token: String) {
		super.onNewToken(token)
		Log.d("token", "Token: $token")
		
		// Save new token to Realtime Database
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val currentUser = authService.getCurrentUser()
				if (currentUser != null) {
					val updates = mapOf("token" to token)
					alumniRepo.updateProfile(currentUser.id, updates)
					Log.d("token", "Token saved to Realtime Database")
				}
			} catch (e: Exception) {
				Log.e("token", "Failed to save token", e)
			}
		}
	}

	override fun onMessageReceived(message: RemoteMessage) {
		super.onMessageReceived(message)
		
		Log.d("token", "Message received from: ${message.from}")

		// Extract notification data
		val title = message.notification?.title ?: "Alumni Directory"
		val body = message.notification?.body ?: "You have a new notification"
		val notificationType = message.data["type"] // "approval" or "rejection"
		
		Log.d("token", "Notification - Title: $title, Body: $body, Type: $notificationType")

		// Show notification
		showNotification(title, body)
	}

	private fun showNotification(title: String, body: String) {
		val channelId = "ian_alumni_notifications"
		val channelName = "Alumni Directory Notifications"
		
		// Create notification channel
		val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		val channel = NotificationChannel(
			channelId,
			channelName,
			NotificationManager.IMPORTANCE_HIGH
		).apply {
			description = "Notifications for alumni registration approval/rejection"
		}
		notificationManager.createNotificationChannel(channel)

		// Create intent to open app when notification is tapped
		val intent = Intent(this, MainActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		}
		val pendingIntent = PendingIntent.getActivity(
			this,
			0,
			intent,
			PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
		)

		// Build notification
		val notification = NotificationCompat.Builder(this, channelId)
			.setContentTitle(title)
			.setContentText(body)
			.setSmallIcon(R.drawable.ic_launcher_foreground)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setAutoCancel(true)
			.setContentIntent(pendingIntent)
			.build()

		// Show notification
		notificationManager.notify(System.currentTimeMillis().toInt(), notification)
	}
}