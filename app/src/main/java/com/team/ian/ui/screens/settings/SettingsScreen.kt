package com.team.ian.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
	navController: NavController
) {
	val viewModel: SettingsViewModel = viewModel()
	val alumni = viewModel.alumni.collectAsStateWithLifecycle().value
	val showEmail = remember(alumni.showEmail) { mutableStateOf(alumni.showEmail) }
	val showPhone = remember(alumni.showPhone) { mutableStateOf(alumni.showPhone) }
	val showLinkedIn = remember(alumni.showLinkedIn) { mutableStateOf(alumni.showLinkedIn) }
	val showGithub = remember(alumni.showGithub) { mutableStateOf(alumni.showGithub) }

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
					.padding(24.dp),
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Text(
					text = "Privacy Controls",
					style = MaterialTheme.typography.headlineMedium,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.primary
				)

				Column(
					modifier = Modifier.fillMaxWidth(),
					verticalArrangement = Arrangement.spacedBy(16.dp)
				) {
					PrivacyControl(
						label = "Show my email to others",
						checked = showEmail.value,
						onCheckedChange = { showEmail.value = it }
					)
					PrivacyControl(
						label = "Show my phone number to others",
						checked = showPhone.value,
						onCheckedChange = { showPhone.value = it }
					)
					PrivacyControl(
						label = "Show my LinkedIn to others",
						checked = showLinkedIn.value,
						onCheckedChange = { showLinkedIn.value = it }
					)
					PrivacyControl(
						label = "Show my Github to others",
						checked = showGithub.value,
						onCheckedChange = { showGithub.value = it }
					)
				}

				Spacer(Modifier.height(8.dp))

				Button(
					onClick = {
						viewModel.updatePrivacyControls(
							showEmail = showEmail.value,
							showPhone = showPhone.value,
							showLinkedIn = showLinkedIn.value,
							showGithub = showGithub.value
						)
						navController.popBackStack()
					},
					modifier = Modifier
						.fillMaxWidth()
						.height(50.dp),
					shape = RoundedCornerShape(12.dp)
				) {
					Text(
						text = "Save",
						style = MaterialTheme.typography.bodyLarge,
						fontWeight = FontWeight.Bold
					)
				}
			}
		}
	}
}

@Composable
private fun PrivacyControl(
	label: String,
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = label,
			modifier = Modifier.weight(1f),
			style = MaterialTheme.typography.bodyLarge
		)
		Switch(
			checked = checked,
			onCheckedChange = onCheckedChange
		)
	}
}