package com.team.ian.ui.screens.viewProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ViewProfileScreen(
	navController: NavController
) {
	val viewModel: ViewProfileViewModel = hiltViewModel()
	val alumni = viewModel.alumni.collectAsStateWithLifecycle().value
	val context = LocalContext.current

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
				// Profile Photo
				Box(
					modifier = Modifier
						.fillMaxWidth(0.4f)
						.aspectRatio(1f)
						.clip(CircleShape)
				) {
					if (alumni.photoURL.isNotBlank()) {
						AsyncImage(
							model = ImageRequest.Builder(context)
								.data(alumni.photoURL)
								.crossfade(true)
								.build(),
							contentDescription = "Profile photo",
							modifier = Modifier.fillMaxSize()
						)
					} else {
						Box(
							modifier = Modifier
								.fillMaxSize()
								.background(
									MaterialTheme.colorScheme.surfaceVariant,
									CircleShape
								),
							contentAlignment = Alignment.Center
						) {
							Icon(
								imageVector = Icons.Default.Person,
								contentDescription = "No profile photo",
								modifier = Modifier.size(64.dp),
								tint = MaterialTheme.colorScheme.onSurfaceVariant
							)
						}
					}
				}

				Spacer(Modifier.height(24.dp))

				Text(
					text = alumni.fullName,
					style = MaterialTheme.typography.headlineMedium,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.primary
				)

				Spacer(Modifier.height(24.dp))
				HorizontalDivider()
				Spacer(Modifier.height(16.dp))

				// Basic Info
				Column(
					modifier = Modifier.fillMaxWidth(),
					verticalArrangement = Arrangement.spacedBy(12.dp)
				) {
					if (alumni.showEmail) {
						InfoRow("Email", alumni.email)
					}
					InfoRow("Graduation Year", alumni.graduationYear.toString())
					InfoRow("Department", alumni.department)
				}

				Spacer(Modifier.height(16.dp))
				HorizontalDivider()
				Spacer(Modifier.height(16.dp))

				// Professional Info
				Column(
					modifier = Modifier.fillMaxWidth(),
					verticalArrangement = Arrangement.spacedBy(12.dp)
				) {
					InfoRow("Job Title", alumni.jobTitle)
					InfoRow("Company", alumni.company)
					InfoRow("Tech Stack", alumni.primaryStack)
				}

				Spacer(Modifier.height(16.dp))
				HorizontalDivider()
				Spacer(Modifier.height(16.dp))

				// Location
				Column(
					modifier = Modifier.fillMaxWidth(),
					verticalArrangement = Arrangement.spacedBy(12.dp)
				) {
					InfoRow("City", alumni.city)
					InfoRow("Country", alumni.country)
				}

				if (alumni.linkedin.isNotBlank() || alumni.github.isNotBlank()) {
					Spacer(Modifier.height(16.dp))
					HorizontalDivider()
					Spacer(Modifier.height(16.dp))

					// Links
					Column(
						modifier = Modifier.fillMaxWidth(),
						verticalArrangement = Arrangement.spacedBy(12.dp)
					) {
						if (alumni.linkedin.isNotBlank()) {
							InfoRow("LinkedIn", alumni.linkedin)
						}
						if (alumni.github.isNotBlank()) {
							InfoRow("GitHub", alumni.github)
						}
					}
				}
			}
		}
	}
}

@Composable
fun InfoRow(label: String, value: String) {
	Column(modifier = Modifier.padding(vertical = 4.dp)) {
		Text(
			text = label,
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Text(
			text = value.ifBlank { "-" },
			style = MaterialTheme.typography.bodyLarge
		)
	}
}