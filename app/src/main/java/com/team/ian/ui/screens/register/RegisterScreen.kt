package com.team.ian.ui.screens.register

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(24.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
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
					onSubmit = { submitRegistration() },
					onBack = { page = 1 }
				)
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
	Column(horizontalAlignment = Alignment.CenterHorizontally) {

		OutlinedTextField(
			value = registration.name,
			onValueChange = { onUpdate(registration.copy(name = it)) },
			label = { Text("Full Name") }
		)

		OutlinedTextField(
			value = registration.email,
			onValueChange = { onUpdate(registration.copy(email = it)) },
			label = { Text("Email") }
		)

		OutlinedTextField(
			value = password,
			onValueChange = onPasswordChange,
			label = { Text("Password") },
			visualTransformation = PasswordVisualTransformation()
		)

		OutlinedTextField(
			value = registration.gradYear.toString(),
			onValueChange = {
				onUpdate(registration.copy(gradYear = it.toIntOrNull() ?: 0))
			},
			label = { Text("Graduation Year") }
		)

		OutlinedTextField(
			value = registration.department,
			onValueChange = { onUpdate(registration.copy(department = it)) },
			label = { Text("Department / Major") }
		)

		Spacer(Modifier.height(16.dp))
		Button(onClick = onNext) { Text("Next") }
	}
}

@Composable
fun ProfessionalInfoPage(
	registration: Registration,
	onUpdate: (Registration) -> Unit,
	onNext: () -> Unit,
	onBack: () -> Unit
) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {

		OutlinedTextField(
			value = registration.position,
			onValueChange = { onUpdate(registration.copy(position = it)) },
			label = { Text("Current Position") }
		)

		OutlinedTextField(
			value = registration.organization,
			onValueChange = { onUpdate(registration.copy(organization = it)) },
			label = { Text("Company / Organization") }
		)

		OutlinedTextField(
			value = registration.techStack,
			onValueChange = { onUpdate(registration.copy(techStack = it)) },
			label = { Text("Primary Tech Stack") }
		)

		Spacer(Modifier.height(16.dp))
		Button(onClick = onNext) { Text("Next") }
		TextButton(onClick = onBack) { Text("Back") }
	}
}

@Composable
fun LocationInfoPage(
	registration: Registration,
	onUpdate: (Registration) -> Unit,
	onSubmit: () -> Unit,
	onBack: () -> Unit
) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {

		OutlinedTextField(
			value = registration.city,
			onValueChange = { onUpdate(registration.copy(city = it)) },
			label = { Text("City") }
		)

		OutlinedTextField(
			value = registration.country,
			onValueChange = { onUpdate(registration.copy(country = it)) },
			label = { Text("Country") }
		)

		Spacer(Modifier.height(16.dp))
		Button(onClick = onSubmit) { Text("Register") }
		TextButton(onClick = onBack) { Text("Back") }
	}
}

//@Composable
//fun AdditionalInfo(
//    extendedInfo: ExtendedInfo,
//    onUpdateData: (ExtendedInfo) -> Unit,
//    onBack: () -> Unit
//) {
//    Text("Past Job History")
//    OutlinedTextField(
//        value = extendedInfo.pastJobHistory,
//        onValueChange = {
//            onUpdateData(
//                extendedInfo.copy(pastJobHistory = it)
//            )
//        }
//    )
//    Spacer(Modifier.padding(5.dp))
//    Text("Skills")
//    OutlinedTextField(
//        value = extendedInfo.skills,
//        onValueChange = {
//            onUpdateData(
//                extendedInfo.copy(skills = it)
//            )
//        }
//    )
//    Spacer(Modifier.padding(5.dp))
//    Text("Short Bio")
//    OutlinedTextField(
//        value = extendedInfo.shortBio,
//        onValueChange = {
//            onUpdateData(
//                extendedInfo.copy(shortBio = it)
//            )
//        }
//    )
//    Spacer(Modifier.padding(5.dp))
//    Text("Profile Picture")
//    OutlinedTextField(
//        value = extendedInfo.profilePicUrl,
//        onValueChange = {
//            onUpdateData(
//                extendedInfo.copy(profilePicUrl = it)
//            )
//        }
//    )
//    Spacer(modifier = Modifier.height(16.dp))
//    Button(onClick = {}) {
//        Text("Register")
//    }
//    Button(onClick = {
//        onBack()
//    }) {
//        Text("Back")
//    }
//}

