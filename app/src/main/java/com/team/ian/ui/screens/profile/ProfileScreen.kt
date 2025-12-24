package com.team.ian.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.ian.service.AuthService
import com.team.ian.ui.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
	navController: NavController
) {
	val context = LocalContext.current
	val authService = AuthService.getInstance()
	val user = authService.user.collectAsStateWithLifecycle().value
	val scope = rememberCoroutineScope()

	fun signOut() {
		scope.launch {
			authService.signOut()
			navController.navigate(Screen.Login) {
				popUpTo(Screen.Home) { inclusive = true }
			}
		}
	}

	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier.fillMaxSize()
	) {
		if (user == null) {
			Text("No user logged in")
		} else {
			Column(
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
							.fillMaxWidth(0.5f)
							.aspectRatio(1f)
							.clip(CircleShape)
					)

					Spacer(Modifier.height(16.dp))
				}

				Text(
					text = user.name.ifBlank { "User" }
				)

				Spacer(Modifier.height(8.dp))

				Text(user.email)

				Spacer(Modifier.height(24.dp))

				Button(onClick = { signOut() }) {
					Text("Sign out")
				}
			}
		}
	}
}
