package com.team.ian.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun InfoRow(
	label: String,
	value: String,
	icon: ImageVector,
	onClick: (() -> Unit)? = null
) {
	val clickableModifier = if (onClick != null && value.isNotBlank()) {
		Modifier.clickable { onClick() }
	} else {
		Modifier
	}
	Column(
		modifier = Modifier
			.padding(vertical = 4.dp)
			.then(clickableModifier))
	{
		Text(
			text = label,
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Text(
			text = value.ifBlank { "-" },
			style = MaterialTheme.typography.bodyLarge,
			color = if (onClick != null && value.isNotBlank()) {
				MaterialTheme.colorScheme.primary
			} else {
				MaterialTheme.colorScheme.onSurface
			}
		)
	}
}