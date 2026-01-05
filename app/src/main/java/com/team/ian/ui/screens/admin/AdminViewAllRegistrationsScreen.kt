package com.team.ian.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.ian.ui.navigation.Screen

@Composable
fun AdminViewAllRegistrationsScreen(
	navController: NavController
) {
	val viewModel: AdminViewAllRegistrationsViewModel = viewModel()
	val pendingAlumni = viewModel.pendingAlumni.collectAsStateWithLifecycle().value
	val context = LocalContext.current

	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		if (pendingAlumni.isEmpty()) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				Text(
					text = "No Pending Registrations",
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		} else {
			LazyColumn(
				verticalArrangement = Arrangement.spacedBy(16.dp),
				contentPadding = PaddingValues(16.dp),
				modifier = Modifier.fillMaxSize()
			) {
				items(pendingAlumni) { alumni ->
					Card(
						elevation = CardDefaults.cardElevation(4.dp),
						shape = RoundedCornerShape(16.dp),
						modifier = Modifier
							.fillMaxWidth()
							.clickable {
								navController.navigate(Screen.AdminViewRegistration(alumni.uid))
							}
					) {
						Row(
							modifier = Modifier
								.fillMaxWidth()
								.padding(16.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							// Profile Photo
							Box(
								modifier = Modifier
									.size(64.dp)
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
											modifier = Modifier.size(32.dp),
											tint = MaterialTheme.colorScheme.onSurfaceVariant
										)
									}
								}
							}

							Spacer(Modifier.width(16.dp))

							// Alumni Info
							Column(
								modifier = Modifier.weight(1f)
							) {
								Text(
									text = alumni.fullName,
									style = MaterialTheme.typography.titleMedium,
									fontWeight = FontWeight.Bold,
									color = MaterialTheme.colorScheme.onSurface,
									maxLines = 1,
									overflow = TextOverflow.Ellipsis
								)

								Spacer(Modifier.height(4.dp))

								Text(
									text = alumni.email,
									style = MaterialTheme.typography.bodyMedium,
									color = MaterialTheme.colorScheme.onSurfaceVariant,
									maxLines = 1,
									overflow = TextOverflow.Ellipsis
								)

								Spacer(Modifier.height(4.dp))

								Text(
									text = "${alumni.jobTitle} at ${alumni.company}",
									style = MaterialTheme.typography.bodySmall,
									color = MaterialTheme.colorScheme.onSurfaceVariant,
									maxLines = 1,
									overflow = TextOverflow.Ellipsis
								)
							}
						}
					}
				}
			}
		}
	}
}