package com.team.ian.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.team.ian.data.model.Role
import com.team.ian.service.AuthService
import com.team.ian.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
	navController: NavController,
) {
	LaunchedEffect(Unit) {
		delay(1200)

		val next = if (AuthService.getInstance().getCurrentUser()?.role == Role.ALUMNI) {
			Screen.Home
		} else if (AuthService.getInstance().getCurrentUser()?.role == Role.NONE) {
			Screen.Pending
		} else if (AuthService.getInstance().getCurrentUser()?.role == Role.ADMIN) {
			Screen.AdminDashboard
		} else if (AuthService.getInstance().getCurrentUser() != null) {
			Screen.Login
		} else {
			Screen.Login
		}

		navController.navigate(next) {
			popUpTo(Screen.Splash) { inclusive = true }
			launchSingleTop = true
		}
	}

	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = "Ian²",
			style = MaterialTheme.typography.displayMedium
		)
	}
}