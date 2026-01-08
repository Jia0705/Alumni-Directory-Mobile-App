package com.team.ian.ui.screens.profile

import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.ui.screens.utils.setRefresh

@Composable
fun ExtendedInfoScreen(navController: NavController) {
	val viewModel: ExtendedInfoViewModel = viewModel()
	val extendedInfo = viewModel.extendedInfo.collectAsStateWithLifecycle().value

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.verticalScroll(rememberScrollState())
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 25.dp, vertical = 16.dp),
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
								"No profile image",
								style = MaterialTheme.typography.labelSmall,
								modifier = Modifier.padding(top = 4.dp)
							)
						}
					}
				}
			}
		}

		ProfileSection(
			title = "About",
			icon = Icons.Default.Person,
			modifier = Modifier.padding(horizontal = 25.dp, vertical = 8.dp)
		) {
			Text(
				text = extendedInfo.shortBio,
				style = MaterialTheme.typography.bodyLarge,
				lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
			)
		}

		ProfileSection(
			title = "Skills",
			icon = Icons.Default.Code,
			modifier = Modifier.padding(horizontal = 25.dp, vertical = 8.dp)
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				extendedInfo.skills.filter { it.isNotBlank() }.forEach { skill ->
					ProfileChip(text = skill)
				}
			}
		}

		ProfileSection(
			title = "Work Experience",
			icon = Icons.Default.Work,
			modifier = Modifier.padding(horizontal = 25.dp, vertical = 8.dp)
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				extendedInfo.pastJobHistory.filter { it.isNotBlank() }.forEachIndexed { index, job ->
					JobHistoryItem(
						job = job,
						isFirst = index == 0,
						isLast = index == extendedInfo.pastJobHistory.size - 1
					)
				}
			}
		}
	}
}

@Composable
private fun ProfileSection(
	title: String,
	icon: androidx.compose.ui.graphics.vector.ImageVector,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	Card(
		modifier = modifier.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(4.dp),
		shape = RoundedCornerShape(16.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(20.dp)
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					imageVector = icon,
					contentDescription = title,
					modifier = Modifier.size(20.dp),
					tint = MaterialTheme.colorScheme.primary
				)
				Text(
					text = title,
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.SemiBold,
					modifier = Modifier.padding(start = 12.dp)
				)
			}

			Spacer(modifier = Modifier.height(12.dp))

			content()
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
			.clip(RoundedCornerShape(16.dp))
			.background(MaterialTheme.colorScheme.primaryContainer)
			.padding(horizontal = 12.dp, vertical = 6.dp)
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.labelMedium,
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
			modifier = Modifier.width(24.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			if (!isFirst) {
				Box(
					modifier = Modifier
						.width(2.dp)
						.height(8.dp)
						.background(MaterialTheme.colorScheme.outlineVariant)
				)
			}

			Box(
				modifier = Modifier
					.size(10.dp)
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.primary)
			)

			if (!isLast) {
				Box(
					modifier = Modifier
						.width(2.dp)
						.height(8.dp)
						.background(MaterialTheme.colorScheme.outlineVariant)
				)
			}
		}

		Column(
			modifier = Modifier
				.padding(start = 16.dp)
				.fillMaxWidth()
		) {
			Text(
				text = job,
				style = MaterialTheme.typography.bodyLarge,
				modifier = Modifier.fillMaxWidth()
			)
		}
	}
}