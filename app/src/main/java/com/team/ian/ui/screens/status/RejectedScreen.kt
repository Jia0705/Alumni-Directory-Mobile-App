package com.team.ian.ui.screens.status

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
		Card(
			modifier = Modifier.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(4.dp),
			shape = RoundedCornerShape(16.dp)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(32.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Icon(
					imageVector = Icons.Default.Cancel,
					contentDescription = null,
					modifier = Modifier.size(80.dp),
					tint = MaterialTheme.colorScheme.error
				)

				Spacer(Modifier.height(24.dp))

				Text(
					text = "Registration Rejected",
					style = MaterialTheme.typography.headlineMedium,
					fontWeight = FontWeight.Bold,
					textAlign = TextAlign.Center,
					color = MaterialTheme.colorScheme.error
				)

				Spacer(Modifier.height(16.dp))

				Text(
					text = "Unfortunately, your registration was not approved",
					style = MaterialTheme.typography.bodyLarge,
					textAlign = TextAlign.Center
				)

				Spacer(Modifier.height(8.dp))

				Text(
					text = "If you believe this is a mistake, please contact the administrator",
					style = MaterialTheme.typography.bodyMedium,
					textAlign = TextAlign.Center,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				Spacer(Modifier.height(32.dp))

				Button(
					onClick = {
						authService.signOut()
						navController.navigate(Screen.Splash) {
							popUpTo(Screen.Splash) { inclusive = true }
							launchSingleTop = true
						}
					},
					modifier = Modifier
						.fillMaxWidth()
						.height(50.dp),
					shape = RoundedCornerShape(12.dp)
				) {
					Text(
						text = "Back to Login",
						style = MaterialTheme.typography.bodyLarge,
						fontWeight = FontWeight.Bold
					)
				}
			}
		}
	}
}