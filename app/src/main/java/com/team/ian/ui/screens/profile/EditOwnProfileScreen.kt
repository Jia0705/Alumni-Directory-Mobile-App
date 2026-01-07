package com.team.ian.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.components.InfoTextField
import com.team.ian.data.model.AlumniField
import com.team.ian.ui.navigation.Screen
import com.team.ian.ui.screens.utils.setRefresh

@Composable
fun EditOwnProfileScreen(
	navController: NavController
) {
	val viewModel: EditOwnProfileViewModel = viewModel()
	val alumni = viewModel.alumni.collectAsStateWithLifecycle().value

	LaunchedEffect(Unit) {
		viewModel.finish.collect {
			setRefresh(navController)
			navController.popBackStack()
		}
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
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
					.verticalScroll(rememberScrollState())
			) {
				Text(
					text = "Edit Profile",
					style = MaterialTheme.typography.headlineMedium,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.primary
				)

				Spacer(Modifier.height(24.dp))

				// Basic Info
				Text(
					text = "Basic Information",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold
				)

				Spacer(Modifier.height(12.dp))

				InfoTextField(
					label = "Full Name",
					value = alumni.fullName,
					onValueChange = {},
					isEditable = false
				)
				InfoTextField(
					label = "Email",
					value = alumni.email,
					onValueChange = {},
					isEditable = false
				)
				InfoTextField(
					label = "Graduation Year",
					value = alumni.graduationYear.toString(),
					onValueChange = {},
					isEditable = false
				)
				InfoTextField(
					label = "Department",
					value = alumni.department,
					onValueChange = {},
					isEditable = false
				)

				Spacer(Modifier.height(16.dp))
				HorizontalDivider()
				Spacer(Modifier.height(16.dp))

				// Professional Info
				Text(
					text = "Professional Information",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold
				)

				Spacer(Modifier.height(12.dp))

				InfoTextField(
					label = "Job Title",
					value = alumni.jobTitle,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.JOB_TITLE, it)
					}
				)
				InfoTextField(
					label = "Company",
					value = alumni.company,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.COMPANY, it)
					}
				)
				InfoTextField(
					label = "Tech Stack",
					value = alumni.primaryStack,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.TECH_STACK, it)
					}
				)

				Spacer(Modifier.height(16.dp))
				HorizontalDivider()
				Spacer(Modifier.height(16.dp))

				// Location
				Text(
					text = "Location",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold
				)

				Spacer(Modifier.height(12.dp))

				InfoTextField(
					label = "City",
					value = alumni.city,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.CITY, it)
					}
				)
				InfoTextField(
					label = "Country",
					value = alumni.country,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.COUNTRY, it)
					}
				)

				Spacer(Modifier.height(16.dp))
				HorizontalDivider()
				Spacer(Modifier.height(16.dp))

				TextButton(onClick = {
					Log.d("debugging", "navigate")
					navController.navigate(
						Screen.AddExtendedInformation(alumni.uid)
					)
				}) {
					Text("Add extended info to your profile?")
				}

				Spacer(Modifier.height(16.dp))

				// Buttons
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(12.dp)
				) {
					Button(
						onClick = {
							viewModel.resetAllStates()
						},
						modifier = Modifier
							.weight(1f)
							.height(50.dp),
						shape = RoundedCornerShape(12.dp)
					) {
						Text(
							text = "Reset",
							style = MaterialTheme.typography.bodyLarge,
							fontWeight = FontWeight.Bold
						)
					}
					OutlinedButton(
						onClick = {
							viewModel.finishEditing()
						},
						modifier = Modifier
							.weight(1f)
							.height(50.dp),
						shape = RoundedCornerShape(12.dp)
					) {
						Text(
							text = "Done",
							style = MaterialTheme.typography.bodyLarge,
							fontWeight = FontWeight.Bold
						)
					}
				}
			}
		}
	}
}