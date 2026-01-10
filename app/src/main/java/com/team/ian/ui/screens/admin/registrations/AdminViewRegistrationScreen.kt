package com.team.ian.ui.screens.admin.registrations

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.team.ian.ui.components.InfoRow

@Composable
fun AdminViewRegistrationScreen(
	navController: NavController
) {
	val viewModel: AdminViewRegistrationViewModel = hiltViewModel()
	val alumni = viewModel.pendingAlumni.collectAsStateWithLifecycle().value

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
					.verticalScroll(rememberScrollState())
			) {
				Text(
					text = "Registration Details",
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

				InfoRow("Full Name", alumni.fullName, Icons.Filled.Email)
				InfoRow("Email", alumni.email, Icons.Filled.Email)
				InfoRow("Graduation Year", alumni.graduationYear.toString(), Icons.Filled.Email)
				InfoRow("Department", alumni.department, Icons.Filled.Email)

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

				InfoRow("Job Title", alumni.jobTitle, Icons.Filled.Email)
				InfoRow("Company", alumni.company, Icons.Filled.Email)
				InfoRow("Tech Stack", alumni.primaryStack, Icons.Filled.Email)

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

				InfoRow("City", alumni.city, Icons.Filled.Email)
				InfoRow("Country", alumni.country, Icons.Filled.Email)

				Spacer(Modifier.height(32.dp))

				// Buttons
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(12.dp)
				) {
					Button(
						onClick = {
							viewModel.approveAlumni()
							navController.popBackStack()
						},
						modifier = Modifier
							.weight(1f)
							.height(50.dp),
						shape = RoundedCornerShape(12.dp)
					) {
						Text(
							text = "Approve",
							style = MaterialTheme.typography.bodyLarge,
							fontWeight = FontWeight.Bold
						)
					}

					OutlinedButton(
						onClick = {
							viewModel.rejectAlumni()
							navController.popBackStack()
						},
						modifier = Modifier
							.weight(1f)
							.height(50.dp),
						shape = RoundedCornerShape(12.dp)
					) {
						Text(
							text = "Reject",
							style = MaterialTheme.typography.bodyLarge,
							fontWeight = FontWeight.Bold
						)
					}
				}
			}
		}
	}
}
