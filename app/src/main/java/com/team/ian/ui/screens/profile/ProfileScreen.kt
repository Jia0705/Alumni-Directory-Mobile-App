package com.team.ian.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.ian.service.AuthService
import com.team.ian.ui.navigation.Screen

@Composable
fun ProfileScreen(
	navController: NavController
) {
	val context = LocalContext.current
	val authService = AuthService.getInstance()
	val user = authService.user.collectAsStateWithLifecycle().value

	fun signOut() {
			authService.signOut()
			navController.navigate(Screen.Splash) {
				popUpTo(Screen.Splash) { inclusive = true }
				launchSingleTop = true
			}
	}

	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxSize()
			.padding(24.dp)
	) {
		if (user == null) {
			Text(
				text = "No user logged in",
				style = MaterialTheme.typography.bodyLarge
			)
		} else {
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

					if (user.photoURL.isNotBlank()) {
						AsyncImage(
							model = ImageRequest.Builder(context)
								.data(user.photoURL)
								.crossfade(true)
								.build(),
							contentDescription = "Profile photo",
							modifier = Modifier
								.fillMaxWidth(0.4f)
								.aspectRatio(1f)
								.clip(CircleShape)
						)

						Spacer(Modifier.height(24.dp))
					}

					Text(
						text = user.name.ifBlank { "User" },
						style = MaterialTheme.typography.headlineMedium,
						fontWeight = FontWeight.Bold,
						color = MaterialTheme.colorScheme.primary
					)

					Spacer(Modifier.height(8.dp))

					Text(
						text = user.email,
						style = MaterialTheme.typography.bodyLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)

					Spacer(Modifier.height(24.dp))

					HorizontalDivider()

					Spacer(Modifier.height(24.dp))

					Button(
						onClick = { signOut() },
						modifier = Modifier
							.fillMaxWidth()
							.height(50.dp),
						shape = RoundedCornerShape(12.dp)
					) {
						Text(
							text = "Sign Out",
							style = MaterialTheme.typography.bodyLarge,
							fontWeight = FontWeight.Bold
						)
					}
				}
			}
		}
	}
}
