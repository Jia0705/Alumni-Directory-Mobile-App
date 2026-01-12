package com.team.ian

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import com.team.ian.ui.navigation.AppNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private val notificationPermissionLauncher = registerForActivityResult(
		ActivityResultContracts.RequestPermission()
	) { /* Permission result handled */ }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		
		// Request notification permission for FCM
		requestNotificationPermission()
		
		setContent {
            ComposeApp()
		}
	}

	private fun requestNotificationPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			if (ActivityCompat.checkSelfPermission(
					this,
					POST_NOTIFICATIONS
				) != PERMISSION_GRANTED
			) {
				notificationPermissionLauncher.launch(POST_NOTIFICATIONS)
			}
		}
	}
}

@Composable
fun ComposeApp() {
	AppNav()
}