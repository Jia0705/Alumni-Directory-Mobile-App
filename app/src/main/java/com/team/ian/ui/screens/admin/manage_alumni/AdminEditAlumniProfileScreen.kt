package com.team.ian.ui.screens.admin.manage_alumni

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.team.ian.ui.components.InfoTextField
import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.AlumniField
import com.team.ian.data.model.Role
import com.team.ian.ui.screens.utils.setRefresh

@Composable
fun AdminEditAlumniProfileScreen(
	navController: NavController
) {
	val viewModel: AdminEditAlumniProfileViewModel = hiltViewModel()
	val alumni = viewModel.alumni.collectAsStateWithLifecycle().value
	var initialAlumni by remember { mutableStateOf(Alumni()) }
	var initialSet by remember { mutableStateOf(false) }
	var showDiscardDialog by remember { mutableStateOf(false) }
	var pendingRole by remember { mutableStateOf<Role?>(null) }

	LaunchedEffect(Unit) {
		viewModel.finish.collect {
			setRefresh(navController)
			navController.popBackStack()
		}
	}

	LaunchedEffect(alumni.uid) {
		if (!initialSet && alumni.uid.isNotBlank()) {
			initialAlumni = alumni
			initialSet = true
		}
	}

	val hasChanges = initialSet && alumni != initialAlumni

	BackHandler {
		if (hasChanges) {
			showDiscardDialog = true
		} else {
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
					text = "Alumni Profile",
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
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.FULL_NAME, it)
					}
				)
				InfoTextField(
					label = "Email",
					value = alumni.email,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.EMAIL, it)
					}
				)
				InfoTextField(
					label = "Graduation Year",
					value = alumni.graduationYear.toString(),
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.GRAD_YEAR, it)
					}
				)
				InfoTextField(
					label = "Department",
					value = alumni.department,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.DEPARTMENT, it)
					}
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

				Text(
					text = "Contact Methods",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold
				)

				Spacer(Modifier.height(12.dp))

				InfoTextField(
					label = "LinkedIn",
					value = alumni.linkedin,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.LINKEDIN, it)
					}
				)
				InfoTextField(
					label = "GitHub",
					value = alumni.github,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.GITHUB, it)
					}
				)
				InfoTextField(
					label = "Phone",
					value = alumni.phone,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.PHONE, it)
					}
				)

				Spacer(Modifier.height(16.dp))
				HorizontalDivider()
				Spacer(Modifier.height(16.dp))

				Text(
					text = "Extended Info",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold
				)

				Spacer(Modifier.height(12.dp))

				InfoTextField(
					label = "About / Bio",
					value = alumni.shortBio,
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.SHORT_BIO, it)
					}
				)
				InfoTextField(
					label = "Skills (comma separated)",
					value = alumni.skills.joinToString(", "),
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.SKILLS, it)
					}
				)
				InfoTextField(
					label = "Work Experience (comma separated)",
					value = alumni.pastJobHistory.joinToString(", "),
					onValueChange = {
						viewModel.updateAlumniField(AlumniField.PAST_JOB_HISTORY, it)
					}
				)

				HorizontalDivider()
				Spacer(Modifier.height(16.dp))

				Text(
					text = "Account Status",
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold
				)
				Spacer(Modifier.height(12.dp))
				Row(
					modifier = Modifier.fillMaxWidth()
				) {
					Column(
						Modifier
							.weight(1f)
							.background(
								if (alumni.status == AccountStatus.APPROVED) {
									Color.Green
								} else {
									Color.Transparent
								}
							, RoundedCornerShape(8.dp))
							.fillMaxHeight()
							.padding(vertical = 12.dp)
							.clickable {
								viewModel.updateAlumniStatusState(AccountStatus.APPROVED)
							},
						horizontalAlignment = Alignment.CenterHorizontally,
					) {
						Text(
							text = "Approved",
							color = Color.DarkGray,
							fontWeight = FontWeight.Bold
						)
					}
					Column(
						Modifier
							.weight(1f)
							.background(
								if (alumni.status == AccountStatus.REJECTED) {
									Color.Red
								} else {
									Color.Transparent
								}
							, RoundedCornerShape(8.dp))
							.fillMaxHeight()
							.padding(vertical = 12.dp)
							.clickable {
								viewModel.updateAlumniStatusState(AccountStatus.REJECTED)
							},
						horizontalAlignment = Alignment.CenterHorizontally,
					) {
						Text(
							text = "Rejected",
							color = Color.DarkGray,
							fontWeight = FontWeight.Bold
						)
					}
					Column(
						Modifier
							.weight(1f)
							.background(
								if (alumni.status == AccountStatus.INACTIVE) {
									Color.Gray
								} else {
									Color.Transparent
								}
							, RoundedCornerShape(8.dp))
							.fillMaxHeight()
							.padding(vertical = 12.dp)
							.clickable {
								viewModel.updateAlumniStatusState(AccountStatus.INACTIVE)
							},
						horizontalAlignment = Alignment.CenterHorizontally,
					) {
						Text(
							text = "Inactive",
							color = Color.DarkGray,
							fontWeight = FontWeight.Bold
						)
					}
				}

				Spacer(Modifier.height(16.dp))
				HorizontalDivider()
				Spacer(Modifier.height(16.dp))

				Text(
					text = "Account Role",
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold
				)
				Spacer(Modifier.height(12.dp))
				Row(
					modifier = Modifier.fillMaxWidth()
				) {
					if (alumni.role == Role.ALUMNI) {
						Column(
							Modifier
								.weight(1f)
								.background(Color.LightGray, RoundedCornerShape(8.dp))
								.fillMaxHeight()
								.padding(vertical = 12.dp)
								.clickable { pendingRole = Role.ADMIN },
							horizontalAlignment = Alignment.CenterHorizontally,
						) {
						Text(
							text = "Promote to Admin",
							color = Color.DarkGray,
							fontWeight = FontWeight.Bold
						)
					}
					} else {
						Column(
							Modifier
								.weight(1f)
								.background(Color.Red, RoundedCornerShape(8.dp))
								.fillMaxHeight()
								.padding(vertical = 12.dp)
								.clickable {
									pendingRole = Role.ALUMNI
								},
						horizontalAlignment = Alignment.CenterHorizontally,
					) {
						Text(
							text = "Demote to Alumni",
							color = Color.White,
							fontWeight = FontWeight.Bold
						)
					}
				}
			}

				Spacer(Modifier.height(32.dp))

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
							text = "Reset all",
							style = MaterialTheme.typography.bodyLarge,
							fontWeight = FontWeight.Bold
						)
					}
					OutlinedButton(
						onClick = {
							viewModel.finishEditing()
							navController.popBackStack()
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

	if (showDiscardDialog) {
		AlertDialog(
			onDismissRequest = { showDiscardDialog = false },
			title = { Text("Discard changes?") },
			text = { Text("You have unsaved changes.") },
			confirmButton = {
				TextButton(
					onClick = {
						showDiscardDialog = false
						navController.popBackStack()
					}
				) {
					Text("Discard")
				}
			},
			dismissButton = {
				TextButton(onClick = { showDiscardDialog = false }) {
					Text("Keep Editing")
				}
			}
		)
	}

	if (pendingRole != null) {
		val nextRole = pendingRole ?: Role.ALUMNI
		AlertDialog(
			onDismissRequest = { pendingRole = null },
			title = { Text("Change role?") },
			text = {
				Text(
					text = "Are you sure you want to set this user to ${nextRole.name.lowercase()}?"
				)
			},
			confirmButton = {
				TextButton(
					onClick = {
						viewModel.updateAlumniRole(nextRole)
						pendingRole = null
					}
				) {
					Text("Confirm")
				}
			},
			dismissButton = {
				TextButton(onClick = { pendingRole = null }) {
					Text("Cancel")
				}
			}
		)
	}
}