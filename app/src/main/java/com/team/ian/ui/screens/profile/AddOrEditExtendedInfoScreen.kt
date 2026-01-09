package com.team.ian.ui.screens.profile

import android.R
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.ui.screens.utils.setRefresh
import kotlin.text.set

@Composable
fun AddOrEditExtendedInfoScreen(navController: NavController) {
	val viewModel: AddOrEditExtendedInfoViewModel = viewModel()
	val extendedInfo by viewModel.extendedInfo.collectAsStateWithLifecycle()
	var shortBio by remember { mutableStateOf("") }
	var skills by remember { mutableStateOf(listOf("")) }
	var pastJobHistory by remember { mutableStateOf(listOf("")) }

	// Populate form fields when extendedInfo is loaded
	LaunchedEffect(extendedInfo) {
		extendedInfo?.let { info ->
			shortBio = info.shortBio
			skills = if (info.skills.isNotEmpty()) info.skills else listOf("")
			pastJobHistory = if (info.pastJobHistory.isNotEmpty()) info.pastJobHistory else listOf("")
		}
	}

	// Handle navigation on finish
	LaunchedEffect(Unit) {
		viewModel.finish.collect {
			setRefresh(navController)
			navController.popBackStack()
		}
	}

	fun finishEdit() {
		viewModel.addExtendedInfoToAlumni(
			pastJobHistory,
			skills,
			shortBio
		)
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		Text(
			modifier = Modifier.padding(start=16.dp,top = 20.dp, bottom=5.dp),
			text = "Edit Extended Profile",
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.primary,
		)

		Column(
			modifier = Modifier
				.weight(1f)
				.verticalScroll(rememberScrollState())
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			Card(
				modifier = Modifier.fillMaxWidth(),
				elevation = CardDefaults.cardElevation(4.dp),
				shape = RoundedCornerShape(12.dp),
			) {
				Column(modifier = Modifier.padding(16.dp)) {
					Text(
						"Short Bio",
						style = MaterialTheme.typography.titleMedium,
						color = MaterialTheme.colorScheme.primary,
						modifier = Modifier.padding(bottom = 12.dp)
					)
					OutlinedTextField(
						value = shortBio,
						onValueChange = { shortBio = it },
						modifier = Modifier.fillMaxWidth(),
						placeholder = { Text("Tell us about yourself...") },
						shape = RoundedCornerShape(12.dp),
						minLines = 5
					)
				}
			}

			// Past Job History Section
			Card(
				modifier = Modifier.fillMaxWidth(),
				elevation = CardDefaults.cardElevation(2.dp),
				shape = RoundedCornerShape(12.dp),
			) {
				Column(modifier = Modifier.padding(16.dp)) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							"Past Job History",
							style = MaterialTheme.typography.titleMedium,
							color = MaterialTheme.colorScheme.primary
						)
						OutlinedButton(
							onClick = {
								val updatedList = pastJobHistory.toMutableList()
								updatedList.add("")
								pastJobHistory = updatedList
							},
							modifier = Modifier.height(36.dp)
						) {
							Icon(
								Icons.Default.Add,
								contentDescription = "Add job",
								modifier = Modifier.size(18.dp)
							)
							Spacer(modifier = Modifier.size(4.dp))
							Text("Add")
						}
					}

					Spacer(modifier = Modifier.height(12.dp))

					if (pastJobHistory.isEmpty()) {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 16.dp),
							contentAlignment = Alignment.Center
						) {
							Text(
								"No job entries yet. Click 'Add' to get started.",
								style = MaterialTheme.typography.bodyMedium,
								color = MaterialTheme.colorScheme.onSurfaceVariant
							)
						}
					} else {
						pastJobHistory.forEachIndexed { index, job ->
							Row(
								modifier = Modifier
									.fillMaxWidth()
									.padding(bottom = 8.dp),
								horizontalArrangement = Arrangement.spacedBy(8.dp)
							) {
								OutlinedTextField(
									value = job,
									onValueChange = {
										val updatedList = pastJobHistory.toMutableList()
										updatedList[index] = it
										pastJobHistory = updatedList
									},
									modifier = Modifier.weight(1f),
									placeholder = { Text("Job Title & Company") },
									leadingIcon = {
										Icon(
											Icons.Default.Work,
											contentDescription = "job"
										)
									},
									shape = RoundedCornerShape(12.dp),
									singleLine = true
								)
								if (pastJobHistory.size > 1) {
									OutlinedButton(
										onClick = {
											val updatedList = pastJobHistory.toMutableList()
											updatedList.removeAt(index)
											pastJobHistory = updatedList
										},
										modifier = Modifier.size(56.dp),
										shape = RoundedCornerShape(12.dp),
										colors = ButtonDefaults.outlinedButtonColors(
											contentColor = MaterialTheme.colorScheme.error
										)
									) {
										Icon(
											Icons.Default.Delete,
											contentDescription = "Remove",
											modifier = Modifier.size(20.dp)
										)
									}
								}
							}
						}
					}
				}
			}

			// Skills Section
			Card(
				modifier = Modifier.fillMaxWidth(),
				elevation = CardDefaults.cardElevation(2.dp),
				shape = RoundedCornerShape(12.dp),
			) {
				Column(modifier = Modifier.padding(16.dp)) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							"Skills",
							style = MaterialTheme.typography.titleMedium,
							color = MaterialTheme.colorScheme.primary
						)
						OutlinedButton(
							onClick = {
								val updatedList = skills.toMutableList()
								updatedList.add("")
								skills = updatedList
							},
							modifier = Modifier.height(36.dp)
						) {
							Icon(
								Icons.Default.Add,
								contentDescription = "Add skill",
								modifier = Modifier.size(18.dp)
							)
							Spacer(modifier = Modifier.size(4.dp))
							Text("Add")
						}
					}

					Spacer(modifier = Modifier.height(12.dp))

					if (skills.isEmpty()) {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 16.dp),
							contentAlignment = Alignment.Center
						) {
							Text(
								"No skills yet. Click 'Add' to get started.",
								style = MaterialTheme.typography.bodyMedium,
								color = MaterialTheme.colorScheme.onSurfaceVariant
							)
						}
					} else {
						skills.forEachIndexed { index, skill ->
							Row(
								modifier = Modifier
									.fillMaxWidth()
									.padding(bottom = 8.dp),
								horizontalArrangement = Arrangement.spacedBy(8.dp)
							) {
								OutlinedTextField(
									value = skill,
									onValueChange = {
										val updatedList = skills.toMutableList()
										updatedList[index] = it
										skills = updatedList
									},
									modifier = Modifier.weight(1f),
									placeholder = { Text("Enter a skill") },
									shape = RoundedCornerShape(12.dp),
									singleLine = true
								)
								if (skills.size > 1) {
									OutlinedButton(
										onClick = {
											val updatedList = skills.toMutableList()
											updatedList.removeAt(index)
											skills = updatedList
										},
										modifier = Modifier.size(56.dp),
										shape = RoundedCornerShape(12.dp),
										colors = ButtonDefaults.outlinedButtonColors(
											contentColor = MaterialTheme.colorScheme.error
										)
									) {
										Icon(
											Icons.Default.Delete,
											contentDescription = "Remove",
											modifier = Modifier.size(20.dp)
										)
									}
								}
							}
						}
					}
				}
			}
		}

		// Bottom Button
		Button(
			onClick = { finishEdit() },
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
				.height(56.dp),
			shape = RoundedCornerShape(12.dp)
		) {
			Icon(
				Icons.Default.Check,
				contentDescription = null,
				modifier = Modifier.size(20.dp)
			)
			Spacer(modifier = Modifier.size(8.dp))
			Text("Save changes", style = MaterialTheme.typography.titleMedium)
		}
	}
}
