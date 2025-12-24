package com.team.ian.ui.screens.status

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.team.ian.service.AuthService
import com.team.ian.ui.navigation.Screen

@Composable
fun RejectedScreen(
	navController: NavController,
	authService: AuthService = AuthService.getInstance()
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(24.dp),
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "Registration Rejected",
				style = MaterialTheme.typography.headlineMedium,
				textAlign = TextAlign.Center,
				color = MaterialTheme.colorScheme.error
			)

			Spacer(Modifier.height(16.dp))

			Text(
				text = "Unfortunately, your registration was not approved.\n\n" +
						"If you believe this is a mistake, please contact the administrator.",
				textAlign = TextAlign.Center
			)

			Spacer(Modifier.height(32.dp))

			Button(
				onClick = {
					authService.signOut()
					navController.navigate(Screen.Login) {
						popUpTo(Screen.Rejected) { inclusive = true }
					}
				}
			) {
				Text("Back to Login")
			}
		}
	}
}