package com.team.ian.ui.screens.admin.registrations

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.team.ian.ui.components.Avatar
import com.team.ian.ui.navigation.Screen

@Composable
fun AdminViewAllRegistrationsScreen(
	navController: NavController
) {
	val viewModel: AdminViewAllRegistrationsViewModel = hiltViewModel()
	val pendingAlumni = viewModel.pendingAlumni.collectAsStateWithLifecycle().value
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
							) {
								Avatar(
									name = alumni.fullName,
									modifier = Modifier.fillMaxSize(),
									colorName = alumni.avatarColor.ifBlank { null }
								)
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

								Spacer(Modifier.height(4.dp))

								Text(
									text = "Year ${alumni.graduationYear} • ${alumni.department}",
									style = MaterialTheme.typography.bodySmall,
									color = MaterialTheme.colorScheme.onSurfaceVariant,
									maxLines = 1,
									overflow = TextOverflow.Ellipsis
								)

								Spacer(Modifier.height(4.dp))

								Text(
									text = "${alumni.city}, ${alumni.country} • ${alumni.primaryStack}",
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