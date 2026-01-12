package com.team.ian.ui.screens.status

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.team.ian.ui.components.StatusScreenContent
import com.team.ian.ui.navigation.Screen

@Composable
fun RejectedScreen(
	navController: NavController
) {
	val viewModel: StatusViewModel = hiltViewModel()

	StatusScreenContent(
		icon = Icons.Default.Cancel,
		iconTint = MaterialTheme.colorScheme.error,
		title = "Registration Rejected",
		message = "Unfortunately, your registration was not approved",
		subMessage = "If you believe this is a mistake, please contact the administrator",
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