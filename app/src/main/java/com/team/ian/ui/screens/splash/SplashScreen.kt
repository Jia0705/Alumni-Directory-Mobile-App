package com.team.ian.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Role
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import com.team.ian.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
	navController: NavController,
	authService: AuthService = AuthService.getInstance(),
	alumniRepo: AlumniRepo = AlumniRepo.getInstance()
) {
	LaunchedEffect(Unit) {
		delay(1200)

		val user = authService.getCurrentUser()

		// Not login
		if (user == null) {
			navigate(navController, Screen.Login)
			return@LaunchedEffect
		}

		// Login and check alumni profile
		val alumni = alumniRepo.getAlumniByUid(user.id)

		when {
			alumni == null -> {
				// Login but never registered
				navigate(navController, Screen.Register)
			}

			alumni.role == Role.ADMIN -> {
				navigate(navController, Screen.Home) // change it back to admin dashboard when u re done
			}

			alumni.status == AccountStatus.PENDING -> {
				navigate(navController, Screen.Pending)
			}

			alumni.status == AccountStatus.REJECTED -> {
				navigate(navController, Screen.Rejected)
			}

			alumni.status == AccountStatus.APPROVED -> {
				navigate(navController, Screen.Home)
			}

			else -> {
				navigate(navController, Screen.Login)
			}
		}
	}

	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Icon(
				imageVector = Icons.Default.School,
				contentDescription = null,
				modifier = Modifier.size(80.dp),
				tint = MaterialTheme.colorScheme.primary
			)

			Spacer(Modifier.height(24.dp))

			Text(
				text = "Ian²",
				style = MaterialTheme.typography.displayLarge,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.primary
			)

			Spacer(Modifier.height(8.dp))

			Text(
				text = "Alumni Directory",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)

			Spacer(Modifier.height(32.dp))

			CircularProgressIndicator()
		}
	}
}

private fun navigate(
	navController: NavController,
	screen: Screen
) {
	navController.navigate(screen) {
		popUpTo(Screen.Splash) { inclusive = true }
		launchSingleTop = true
	}
}
