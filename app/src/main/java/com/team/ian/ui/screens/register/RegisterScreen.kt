package com.team.ian.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.data.model.Registration
import com.team.ian.ui.navigation.Screen

@Composable
fun RegisterScreen(
	navController: NavController
) {
	val viewModel: RegisterViewModel = viewModel()
	val context = LocalContext.current

	var page by remember { mutableIntStateOf(0) }
	var registration by remember { mutableStateOf(Registration()) }
	var password by remember { mutableStateOf("") }

	fun submitRegistration() {
		val missing = mutableListOf<String>()
		if (registration.name.isBlank()) missing.add("Full Name")
		if (registration.email.isBlank()) missing.add("Email")
		if (password.isBlank()) missing.add("Password")
		if (registration.gradYear <= 0) missing.add("Graduation Year")
		if (registration.department.isBlank()) missing.add("Department / Major")
		if (registration.position.isBlank()) missing.add("Current Position")
		if (registration.organization.isBlank()) missing.add("Company / Organization")
		if (registration.techStack.isBlank()) missing.add("Primary Tech Stack")
		if (registration.city.isBlank()) missing.add("City")
		if (registration.country.isBlank()) missing.add("Country")

		if (missing.isNotEmpty()) {
			Toast.makeText(
				context,
				"Please fill: ${missing.joinToString()}",
				Toast.LENGTH_LONG
			).show()
			return
		}

		if (password.length < 6) {
			Toast.makeText(
				context,
				"Password must be at least 6 characters",
				Toast.LENGTH_LONG
			).show()
			return
		}

		viewModel.register(
			registration = registration,
			password = password,
			onSuccess = {
				navController.navigate(Screen.Pending) {
					popUpTo(Screen.Login) { inclusive = true }
				}
			},
			onError = {
				Toast.makeText(context, it, Toast.LENGTH_LONG).show()
			}
		)
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
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
					.padding(24.dp)
					.verticalScroll(rememberScrollState()),
				horizontalAlignment = Alignment.CenterHorizontally
			) {

				Text(
					text = "Create Account",
					style = MaterialTheme.typography.headlineMedium,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.primary
				)

				Spacer(Modifier.height(8.dp))

				Text(
					text = "Step ${page + 1} of 5",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				Spacer(Modifier.height(16.dp))

				LinearProgressIndicator(
					progress = { (page + 1) / 5f },
					modifier = Modifier.fillMaxWidth()
				)

				Spacer(Modifier.height(24.dp))

				when (page) {
					0 -> BasicInfoPage(
						registration = registration,
						password = password,
						onUpdate = { registration = it },
						onPasswordChange = { password = it },
						onNext = { page = 1 }
					)

					1 -> ProfessionalInfoPage(
						registration = registration,
						onUpdate = { registration = it },
						onNext = { page = 2 },
						onBack = { page = 0 }
					)

					2 -> LocationInfoPage(
						registration = registration,
						onUpdate = { registration = it },
						onNext = { page = 3 },
						onBack = { page = 1 }
					)

					3 -> ContactMethodsPage(
						registration = registration,
						onUpdate = { registration = it },
						onNext = { page = 4 },
						onBack = { page = 2 }
					)

					4 -> ExtendedInfoPage(
						registration = registration,
						onUpdate = { registration = it },
						onSubmit = { submitRegistration() },
						onBack = { page = 3 }
					)
				}

				Spacer(Modifier.height(24.dp))

				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = "Already have an account?",
						style = MaterialTheme.typography.bodyMedium
					)
					TextButton(
						onClick = {
							navController.navigate(Screen.Login) {
								popUpTo(Screen.Register) { inclusive = true }
							}
						}
					) {
						Text(
							text = "Login here",
							fontWeight = FontWeight.Bold
						)
					}
				}
			}
		}
	}
}

@Composable
fun BasicInfoPage(
	registration: Registration,
	password: String,
	onUpdate: (Registration) -> Unit,
	onPasswordChange: (String) -> Unit,
	onNext: () -> Unit
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Basic Information",
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold
		)

		Spacer(Modifier.height(24.dp))

		OutlinedTextField(
			value = registration.name,
			onValueChange = { onUpdate(registration.copy(name = it)) },
			label = { Text("Full Name") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = registration.email,
			onValueChange = { onUpdate(registration.copy(email = it)) },
			label = { Text("Email") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = password,
			onValueChange = onPasswordChange,
			label = { Text("Password") },
			visualTransformation = PasswordVisualTransformation(),
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = if (registration.gradYear == 0) "" else registration.gradYear.toString(),
			onValueChange = {
				onUpdate(registration.copy(gradYear = it.toIntOrNull() ?: 0))
			},
			label = { Text("Graduation Year") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = registration.department,
			onValueChange = { onUpdate(registration.copy(department = it)) },
			label = { Text("Department / Major") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(24.dp))

	Button(
		onClick = onNext,
		modifier = Modifier
			.fillMaxWidth()
			.height(50.dp),
		shape = RoundedCornerShape(12.dp),
		enabled = registration.name.isNotBlank() &&
			registration.email.isNotBlank() &&
			password.length >= 6 &&
			registration.gradYear > 0 &&
			registration.department.isNotBlank()
	) {
		Text(
			text = "Next",
			style = MaterialTheme.typography.bodyLarge,
			fontWeight = FontWeight.Bold
			)
		}
	}
}

@Composable
fun ProfessionalInfoPage(
	registration: Registration,
	onUpdate: (Registration) -> Unit,
	onNext: () -> Unit,
	onBack: () -> Unit
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Professional Information",
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold
		)

		Spacer(Modifier.height(24.dp))

		OutlinedTextField(
			value = registration.position,
			onValueChange = { onUpdate(registration.copy(position = it)) },
			label = { Text("Current Position") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = registration.organization,
			onValueChange = { onUpdate(registration.copy(organization = it)) },
			label = { Text("Company / Organization") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = registration.techStack,
			onValueChange = { onUpdate(registration.copy(techStack = it)) },
			label = { Text("Primary Tech Stack") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(24.dp))

	Button(
		onClick = onNext,
		modifier = Modifier
			.fillMaxWidth()
			.height(50.dp),
		shape = RoundedCornerShape(12.dp),
		enabled = registration.position.isNotBlank() &&
			registration.organization.isNotBlank() &&
			registration.techStack.isNotBlank()
	) {
		Text(
			text = "Next",
			style = MaterialTheme.typography.bodyLarge,
			fontWeight = FontWeight.Bold
			)
		}

		Spacer(Modifier.height(12.dp))

		OutlinedButton(
			onClick = onBack,
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			shape = RoundedCornerShape(12.dp)
		) {
			Text("Back")
		}
	}
}

@Composable
fun LocationInfoPage(
	registration: Registration,
	onUpdate: (Registration) -> Unit,
	onNext: () -> Unit,
	onBack: () -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Location Information",
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold
		)

		Spacer(Modifier.height(24.dp))

		OutlinedTextField(
			value = registration.city,
			onValueChange = { onUpdate(registration.copy(city = it)) },
			label = { Text("City") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = registration.country,
			onValueChange = { onUpdate(registration.copy(country = it)) },
			label = { Text("Country") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

	Button(
		onClick = onNext,
		modifier = Modifier
			.fillMaxWidth()
			.height(50.dp),
		shape = RoundedCornerShape(12.dp),
		enabled = registration.city.isNotBlank() &&
			registration.country.isNotBlank()
	) {
		Text(
			text = "Next",
			style = MaterialTheme.typography.bodyLarge,
			fontWeight = FontWeight.Bold
			)
		}

		Spacer(Modifier.height(12.dp))

		OutlinedButton(
			onClick = onBack,
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			shape = RoundedCornerShape(12.dp)
		) {
			Text("Back")
		}

	}
}

@Composable
fun ContactMethodsPage(
	registration: Registration,
	onUpdate: (Registration) -> Unit,
	onNext: () -> Unit,
	onBack: () -> Unit
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Contact Methods",
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold
		)

		Spacer(Modifier.height(24.dp))

		OutlinedTextField(
			value = registration.linkedin,
			onValueChange = { onUpdate(registration.copy(linkedin = it)) },
			label = { Text("LinkedIn (optional)") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = registration.github,
			onValueChange = { onUpdate(registration.copy(github = it)) },
			label = { Text("GitHub (optional)") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = registration.phone,
			onValueChange = { onUpdate(registration.copy(phone = it)) },
			label = { Text("Phone (optional)") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(24.dp))

		Button(
			onClick = onNext,
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			shape = RoundedCornerShape(12.dp)
		) {
			Text(
				text = "Next",
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Bold
			)
		}

		Spacer(Modifier.height(12.dp))

		OutlinedButton(
			onClick = onBack,
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			shape = RoundedCornerShape(12.dp)
		) {
			Text("Back")
		}
	}
}

@Composable
fun ExtendedInfoPage(
	registration: Registration,
	onUpdate: (Registration) -> Unit,
	onSubmit: () -> Unit,
	onBack: () -> Unit
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Extended Info",
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold
		)

		Spacer(Modifier.height(24.dp))

		OutlinedTextField(
			value = registration.shortBio,
			onValueChange = { onUpdate(registration.copy(shortBio = it)) },
			label = { Text("About (optional)") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			minLines = 4
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = registration.skills,
			onValueChange = { onUpdate(registration.copy(skills = it)) },
			label = { Text("Skills (comma separated)") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		OutlinedTextField(
			value = registration.workExperience,
			onValueChange = { onUpdate(registration.copy(workExperience = it)) },
			label = { Text("Work Experience (comma separated)") },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(12.dp),
			singleLine = true
		)

		Spacer(Modifier.height(16.dp))

		Button(
			onClick = onSubmit,
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			shape = RoundedCornerShape(12.dp)
		) {
			Text(
				text = "Complete Registration",
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Bold
			)
		}

		Spacer(Modifier.height(12.dp))

		OutlinedButton(
			onClick = onBack,
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			shape = RoundedCornerShape(12.dp)
		) {
			Text("Back")
		}
	}
}
