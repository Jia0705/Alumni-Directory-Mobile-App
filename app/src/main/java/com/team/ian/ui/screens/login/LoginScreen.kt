package com.team.ian.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.team.ian.ui.navigation.Screen

@Composable
fun LoginScreen(
	navController: NavController,
	viewModel: LoginViewModel = hiltViewModel()
) {
	val context = LocalContext.current

	var email by remember { mutableStateOf("") }
	var pass by remember { mutableStateOf("") }

	fun showToast(msg: String) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
	}

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
					.padding(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {

				Text(
					text = "Ian²",
					style = MaterialTheme.typography.displaySmall,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.primary
				)

				Spacer(Modifier.height(8.dp))

				Text(
					text = "Alumni Directory",
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				Spacer(Modifier.height(32.dp))

				OutlinedTextField(
					value = email,
					onValueChange = { email = it },
					label = { Text("Email") },
					leadingIcon = {
						Icon(Icons.Default.Email, contentDescription = null)
					},
					modifier = Modifier.fillMaxWidth(),
					shape = RoundedCornerShape(12.dp),
					singleLine = true
				)

				Spacer(Modifier.height(16.dp))

				OutlinedTextField(
					value = pass,
					onValueChange = { pass = it },
					label = { Text("Password") },
					leadingIcon = {
						Icon(Icons.Default.Lock, contentDescription = null)
					},
					modifier = Modifier.fillMaxWidth(),
					shape = RoundedCornerShape(12.dp),
					visualTransformation = PasswordVisualTransformation(),
					singleLine = true
				)

				Spacer(Modifier.height(24.dp))

				Button(
					onClick = {
						if (email.isBlank() || pass.isBlank()) {
							showToast("Please fill in all fields")
						} else {
							viewModel.loginWithEmail(
								email = email,
								password = pass,
								onSuccess = {
									navController.navigate(Screen.Splash) {
										popUpTo(Screen.Login) { inclusive = true }
									}
								},
								onError = { showToast(it) }
							)
						}
					},
					modifier = Modifier
						.fillMaxWidth()
						.height(50.dp),
					shape = RoundedCornerShape(12.dp)
				) {
					Text(
						text = "Login",
						style = MaterialTheme.typography.bodyLarge,
						fontWeight = FontWeight.Bold
					)
				}

				Spacer(Modifier.height(16.dp))

				Text(
					text = "OR",
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				Spacer(Modifier.height(16.dp))

				OutlinedButton(
					onClick = {
						viewModel.loginWithGoogle(
							context = context,
							onSuccess = {
								navController.navigate(Screen.Splash) {
									popUpTo(Screen.Login) { inclusive = true }
								}
							},
							onError = { showToast(it) }
						)
					},
					modifier = Modifier
						.fillMaxWidth()
						.height(50.dp),
					shape = RoundedCornerShape(12.dp),
					colors = ButtonDefaults.outlinedButtonColors(
						containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
						contentColor = MaterialTheme.colorScheme.primary
					)
				) {
					Text(
						text = "Sign in with Google",
						style = MaterialTheme.typography.bodyLarge
					)
				}

				Spacer(Modifier.height(24.dp))

				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = "Don't have an account?",
						style = MaterialTheme.typography.bodyMedium
					)
					TextButton(
						onClick = { navController.navigate(Screen.Register) }
					) {
						Text(
							text = "Sign Up",
							fontWeight = FontWeight.Bold
						)
					}
				}
			}
		}
	}
}
