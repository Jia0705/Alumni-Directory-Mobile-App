package com.team.ian.ui.screens.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.ui.components.ProfileSection
import com.team.ian.ui.navigation.Screen
import com.team.ian.ui.screens.utils.setRefresh

@Composable
fun ExtendedInfoScreen(navController: NavController) {
	val viewModel: ExtendedInfoViewModel = viewModel()
	val extendedInfo = viewModel.extendedInfo.collectAsStateWithLifecycle().value

	// Listen for refresh signal from edit screen
	LaunchedEffect(Unit) {
		navController.currentBackStackEntry
			?.savedStateHandle
			?.getStateFlow("refresh", false)
			?.collect { shouldRefresh ->
				if (shouldRefresh) {
					viewModel.getExtendedInfo()
					// Reset the flag
					navController.currentBackStackEntry
						?.savedStateHandle
						?.set("refresh", false)
				}
			}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		Text(
			modifier = Modifier.padding(start = 16.dp, top = 35.dp, bottom = 5.dp),
			text = "Extended Profile",
			style = MaterialTheme.typography.headlineLarge,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.primary,
		)
		Text(
			modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
			text = "View your complete professional profile information",
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
		Column(
			modifier = Modifier
				.weight(1f)
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {

			ProfileSection(
				title = "About",
				icon = Icons.Default.Person
			) {
				Text(
					text = extendedInfo.shortBio.ifBlank { "No bio added yet" },
					style = MaterialTheme.typography.bodyLarge,
					lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4,
					color = if (extendedInfo.shortBio.isBlank()) 
						MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
					else
						MaterialTheme.colorScheme.onSurface
				)
			}

			ProfileSection(
				title = "Skills",
				icon = Icons.Default.Code
			) {
				val skills = extendedInfo.skills.filter { it.isNotBlank() }
				if (skills.isEmpty()) {
					Text(
						text = "No skills added yet",
						style = MaterialTheme.typography.bodyLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
					)
				} else {
					SkillsFlowRow(skills = skills)
				}
			}

			ProfileSection(
				title = "Work Experience",
				icon = Icons.Default.Work
			) {
				val jobs = extendedInfo.pastJobHistory.filter { it.isNotBlank() }
				if (jobs.isEmpty()) {
					Text(
						text = "No work experience added yet",
						style = MaterialTheme.typography.bodyLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
					)
				} else {
					Column(
						verticalArrangement = Arrangement.spacedBy(16.dp)
					) {
						jobs.forEachIndexed { index, job ->
							JobHistoryItem(
								job = job,
								isFirst = index == 0,
								isLast = index == jobs.size - 1
							)
						}
					}
				}
			}

			Spacer(Modifier.height(8.dp))

			Row(
				Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(12.dp)
			) {
				OutlinedButton(
					onClick = {
						setRefresh(navController)
						navController.popBackStack()
					},
					modifier = Modifier
						.weight(1f)
						.height(56.dp),
					shape = RoundedCornerShape(12.dp)
				) {
					Icon(
						Icons.Filled.ArrowBack,
						contentDescription = null,
						modifier = Modifier.size(20.dp)
					)
					Spacer(modifier = Modifier.size(8.dp))
					Text(
						text = "Back",
						style = MaterialTheme.typography.titleMedium
					)
				}
				Button(
					onClick = {
						navController.navigate(Screen.AddOrEditExtendedInfo)
					},
					modifier = Modifier
						.weight(1f)
						.height(56.dp),
					shape = RoundedCornerShape(12.dp)
				) {
					Icon(
						Icons.Filled.Edit,
						contentDescription = null,
						modifier = Modifier.size(20.dp)
					)
					Spacer(modifier = Modifier.size(8.dp))
					Text(
						text = "Edit Profile",
						style = MaterialTheme.typography.titleMedium
					)
				}
			}
			
			Spacer(Modifier.height(16.dp))
		}
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillsFlowRow(skills: List<String>) {
	FlowRow(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		skills.forEach { skill ->
			ProfileChip(text = skill)
		}
	}
}

@Composable
private fun ProfileChip(
	text: String,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.clip(RoundedCornerShape(20.dp))
			.background(MaterialTheme.colorScheme.primaryContainer)
			.padding(horizontal = 16.dp, vertical = 8.dp)
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.bodyMedium,
			fontWeight = FontWeight.Medium,
			color = MaterialTheme.colorScheme.onPrimaryContainer
		)
	}
}

@Composable
private fun JobHistoryItem(
	job: String,
	isFirst: Boolean = false,
	isLast: Boolean = false
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.Top
	) {
		Column(
			modifier = Modifier.width(28.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			if (!isFirst) {
				Box(
					modifier = Modifier
						.width(2.dp)
						.height(12.dp)
						.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
				)
			}

			Box(
				modifier = Modifier
					.size(12.dp)
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.primary)
			)

			if (!isLast) {
				Box(
					modifier = Modifier
						.width(2.dp)
						.weight(1f)
						.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
				)
			}
		}

		Card(
			modifier = Modifier
				.padding(start = 12.dp)
				.fillMaxWidth(),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
			),
			shape = RoundedCornerShape(12.dp)
		) {
			Text(
				text = job,
				style = MaterialTheme.typography.bodyLarge,
				modifier = Modifier.padding(12.dp)
			)
		}
	}
}