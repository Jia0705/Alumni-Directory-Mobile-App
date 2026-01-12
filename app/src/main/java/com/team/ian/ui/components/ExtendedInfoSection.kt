package com.team.ian.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExtendedInfoSection(
	shortBio: String,
	skills: List<String>,
	pastJobHistory: List<String>,
	showEmptyText: Boolean
) {
	if (shortBio.isNotBlank() || showEmptyText) {
		Text(
			text = "Short Bio",
			style = MaterialTheme.typography.labelLarge,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = shortBio.ifBlank { "No bio added yet" },
			style = MaterialTheme.typography.bodyMedium,
			color = if (shortBio.isBlank()) {
				MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
			} else {
				MaterialTheme.colorScheme.onSurface
			}
		)
		Spacer(modifier = Modifier.height(16.dp))
	}

	Text(
		text = "Skills",
		style = MaterialTheme.typography.labelLarge,
		fontWeight = FontWeight.SemiBold,
		color = MaterialTheme.colorScheme.onSurfaceVariant
	)
	Spacer(modifier = Modifier.height(8.dp))
	val skillsFiltered = skills.filter { it.isNotBlank() }
	if (skillsFiltered.isEmpty()) {
		if (showEmptyText) {
			Text(
				text = "No skills added yet",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
			)
		}
	} else {
		SkillsChipRow(skills = skillsFiltered)
	}

	Spacer(modifier = Modifier.height(16.dp))

	Text(
		text = "Work Experience",
		style = MaterialTheme.typography.labelLarge,
		fontWeight = FontWeight.SemiBold,
		color = MaterialTheme.colorScheme.onSurfaceVariant
	)
	Spacer(modifier = Modifier.height(8.dp))
	val jobsFiltered = pastJobHistory.filter { it.isNotBlank() }
	if (jobsFiltered.isEmpty()) {
		if (showEmptyText) {
			Text(
				text = "No work experience added yet",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
			)
		}
	} else {
		JobHistoryChips(jobs = jobsFiltered)
	}
}
