package com.team.ian.ui.screens.profile

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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.ui.screens.utils.setRefresh

@Composable
fun AddOrEditExtendedInfoScreen(navController: NavController) {
	val viewModel: AddOrEditExtendedInfoViewModel = viewModel()
	var selectedImageUri by remember { mutableStateOf<Uri?>(null) } // Stores the Uri of the selected image (uri from gallery)
	var loadedBitmap by remember { mutableStateOf<Bitmap?>(null) } // Stores the actual bitmap after loading
	var isLoading by remember { mutableStateOf(false) }
	var loadError by remember { mutableStateOf(false) }
	var shortBio by remember { mutableStateOf("") }
	var skills by remember { mutableStateOf(listOf("")) }
	var pastJobHistory by remember { mutableStateOf(listOf("")) }
	val context = LocalContext.current

	// TODO: Remove this and decide on storage of image or no storage
//	LaunchedEffect(selectedImageUri) { // Runs when selectedImageUri changes
//		selectedImageUri?.let { uri ->
//			isLoading = true
//			loadError = false
//			loadedBitmap = withContext(Dispatchers.IO) { // Loads bitmap on the background thread
//				try {
//					context.contentResolver.openInputStream(uri)?.use { inputStream -> // Reads image file using the uri
//						BitmapFactory.decodeStream(inputStream) // Converts stream to bitmap object
//					}
//				} catch (e: Exception) {
//					Log.e("debugging", "Error loading bitmap ${e.message}")
//					loadError = true
//					null
//				} finally {
//					isLoading = false
//				}
//			}
//		} ?: run {
//			loadedBitmap = null
//			isLoading = false
//			loadError = false
//		}
//	}

	LaunchedEffect(Unit) {
		viewModel.finish.collect {
			setRefresh(navController)
			navController.popBackStack()
		}
	}

	fun complete() {
		viewModel.addExtendedInfoToAlumni(
			pastJobHistory,
			skills,
			shortBio
		)
	}


	val imagePickerLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent() // Opens device gallery
	) { uri: Uri? -> // Returns a uri pointing to selected image
		uri?.let { selectedImageUri = it }
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(25.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 16.dp),
			elevation = CardDefaults.cardElevation(4.dp),
			shape = RoundedCornerShape(16.dp)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				Box(
					modifier = Modifier.size(150.dp),
					contentAlignment = Alignment.Center
				) {
					when {
						isLoading -> {
							Box(
								modifier = Modifier
									.size(150.dp)
									.clip(CircleShape)
									.background(MaterialTheme.colorScheme.surfaceVariant),
								contentAlignment = Alignment.Center
							) {
								CircularProgressIndicator()
							}
						}
						loadError -> {
							Box(
								modifier = Modifier
									.size(150.dp)
									.clip(CircleShape)
									.background(MaterialTheme.colorScheme.errorContainer),
								contentAlignment = Alignment.Center
							) {
								Column(horizontalAlignment = Alignment.CenterHorizontally) {
									Icon(
										Icons.Default.Error,
										contentDescription = "Error",
										modifier = Modifier.size(48.dp),
										tint = MaterialTheme.colorScheme.error
									)
									Text(
										"Failed to load",
										style = MaterialTheme.typography.bodySmall,
										modifier = Modifier.padding(top = 4.dp)
									)
								}
							}
						}
						loadedBitmap != null -> {
							Image(
								bitmap = loadedBitmap!!.asImageBitmap(),
								contentDescription = "Profile photo",
								modifier = Modifier
									.size(150.dp)
									.clip(CircleShape)
							)
						}
						else -> {
							Box(
								modifier = Modifier
									.size(150.dp)
									.clip(CircleShape)
									.background(
										MaterialTheme.colorScheme.surfaceVariant,
										CircleShape
									),
								contentAlignment = Alignment.Center
							) {
								Column(horizontalAlignment = Alignment.CenterHorizontally) {
									Icon(
										imageVector = Icons.Default.Person,
										contentDescription = "No profile photo",
										modifier = Modifier.size(64.dp),
										tint = MaterialTheme.colorScheme.onSurfaceVariant
									)
									Text(
										"No image selected",
										style = MaterialTheme.typography.labelSmall,
										modifier = Modifier.padding(top = 4.dp)
									)
								}
							}
						}
					}
				}

				Spacer(modifier = Modifier.height(16.dp))

				if (selectedImageUri != null && !isLoading) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						modifier = Modifier.padding(bottom = 8.dp)
					) {
						if (loadedBitmap != null) {
							Icon(
								Icons.Default.Check,
								contentDescription = "Loaded",
								tint = MaterialTheme.colorScheme.primary
							)
							Text(
								"Image loaded successfully",
								style = MaterialTheme.typography.bodySmall,
								color = MaterialTheme.colorScheme.primary
							)
						} else if (loadError) {
							Icon(
								Icons.Default.Error,
								contentDescription = "Error",
								tint = MaterialTheme.colorScheme.error
							)
							Text(
								"Failed to load image",
								style = MaterialTheme.typography.bodySmall,
								color = MaterialTheme.colorScheme.error
							)
						}
					}
				}
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
					Button(
						onClick = { imagePickerLauncher.launch("image/*") },
						modifier = Modifier.fillMaxWidth(),
						enabled = !isLoading
					) {
						Text(if (selectedImageUri != null) "Change Image" else "Select Profile Image")
					}
					if (selectedImageUri != null) {
						Button(
							onClick = {
								selectedImageUri = null
								loadedBitmap = null
								loadError = false
							},
							modifier = Modifier.fillMaxWidth(),
							colors = ButtonDefaults.buttonColors(
								containerColor = MaterialTheme.colorScheme.errorContainer,
								contentColor = MaterialTheme.colorScheme.onErrorContainer
							)
						) {
							Text("Clear Image")
						}
					}
				}
			}
		}

		Card(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
			elevation = CardDefaults.cardElevation(4.dp),
			shape = RoundedCornerShape(16.dp)
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(24.dp)
					.verticalScroll(rememberScrollState()),
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Column {
					Text(
						"Short Bio",
						style = MaterialTheme.typography.titleMedium,
						modifier = Modifier.padding(bottom = 8.dp)
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
				Column {
					Text(
						"Past Job History",
						style = MaterialTheme.typography.titleMedium,
						modifier = Modifier.padding(bottom = 8.dp)
					)
					pastJobHistory.forEachIndexed { index, job ->
						OutlinedTextField(
							value = job,
							onValueChange = {
								val updatedList = pastJobHistory.toMutableList()
								updatedList[index] = it
								pastJobHistory = updatedList
							},
							modifier = Modifier
								.fillMaxWidth()
								.padding(bottom = 8.dp),
							placeholder = { Text("Job Title & Description") },
							leadingIcon = {
								Icon(
									Icons.Default.Work,
									contentDescription = "job title/description"
								)
							},
							shape = RoundedCornerShape(12.dp),
							singleLine = true
						)
					}
					Button(
						onClick = {
							val updatedList = pastJobHistory.toMutableList()
							updatedList.add("")
							pastJobHistory = updatedList
						},
						modifier = Modifier.fillMaxWidth()
					) {
						Text("Add Job Entry")
					}
				}
				Column {
					Text(
						"Skills",
						style = MaterialTheme.typography.titleMedium,
						modifier = Modifier.padding(bottom = 8.dp)
					)
					skills.forEachIndexed { index, skill ->
						OutlinedTextField(
							value = skill,
							onValueChange = {
								val updatedList = skills.toMutableList()
								updatedList[index] = it
								skills = updatedList
							},
							modifier = Modifier
								.fillMaxWidth()
								.padding(bottom = 8.dp),
							placeholder = { Text("Enter a skill") },
							shape = RoundedCornerShape(12.dp),
							singleLine = true
						)
					}
					Button(
						onClick = {
							val updatedList = skills.toMutableList()
							updatedList.add("")
							skills = updatedList
						},
						modifier = Modifier.fillMaxWidth()
					) {
						Text("Add Skill Entry")
					}
				}
			}
		}

		Box(Modifier.fillMaxWidth().padding(16.dp)){
			Button(
				onClick = { complete() },
				modifier = Modifier
					.fillMaxWidth()
					.height(50.dp)
			) {
				Text("Complete Profile")
			}
		}

	}
}