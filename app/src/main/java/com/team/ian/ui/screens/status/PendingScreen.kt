package com.team.ian.ui.screens.status

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.team.ian.ui.components.StatusScreenContent
import com.team.ian.ui.navigation.Screen

@Composable
fun PendingScreen(
	navController: NavController
) {
	val viewModel: StatusViewModel = hiltViewModel()

	StatusScreenContent(
		icon = Icons.Default.HourglassEmpty,
		iconTint = MaterialTheme.colorScheme.primary,
		title = "Account Pending Approval",
		message = "Your registration has been submitted successfully.",
		subMessage = "Please wait for an administrator to approve your account.",
		actionLabel = "Logout",
		onAction = {
			viewModel.signOut()
			navController.navigate(Screen.Splash) {
				popUpTo(Screen.Splash) { inclusive = true }
				launchSingleTop = true
			}
		}
	)
}
