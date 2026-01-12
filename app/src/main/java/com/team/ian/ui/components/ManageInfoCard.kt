package com.team.ian.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ManageInfoCard(
	label: String,
	icon: ImageVector,
	onClick: () -> Unit
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
		),
		shape = RoundedCornerShape(12.dp)
	) {
		TextButton(
			onClick = onClick,
			modifier = Modifier.fillMaxWidth()
		) {
			Icon(
				imageVector = icon,
				contentDescription = null,
				modifier = Modifier.padding(end = 8.dp)
			)
			Text(label)
		}
	}
}
