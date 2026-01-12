package com.team.ian.ui.screens.status

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.team.ian.ui.components.StatusScreenContent
import com.team.ian.ui.navigation.Screen

@Composable
fun InactiveScreen(
	navController: NavController
) {
	val viewModel: StatusViewModel = hiltViewModel()

	StatusScreenContent(
		icon = Icons.Default.PauseCircle,
		iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
		title = "Account Inactive",
		message = "Your account is currently inactive.",
		subMessage = "Contact the administrator if you think this is a mistake.",
		actionLabel = "Back to Login",
		onAction = {
			viewModel.signOut()
			navController.navigate(Screen.Splash) {
				popUpTo(Screen.Splash) { inclusive = true }
				launchSingleTop = true
			}
		}
	)
}
