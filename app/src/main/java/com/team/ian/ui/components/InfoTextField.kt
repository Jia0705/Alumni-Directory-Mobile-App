package com.team.ian.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InfoTextField(
	label: String,
	value: String,
	onValueChange: (String) -> Unit,
	isEditable: Boolean = true,
	icon: ImageVector? = null,
	placeholder: String = ""
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 6.dp),
		colors = CardDefaults.cardColors(
			containerColor = if (isEditable) 
				MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
			else 
				MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
		),
		shape = RoundedCornerShape(16.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
		) {
			Text(
				text = label,
				style = MaterialTheme.typography.labelLarge,
				fontWeight = FontWeight.SemiBold,
				color = if (isEditable) 
					MaterialTheme.colorScheme.primary 
				else 
					MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(bottom = 8.dp)
			)
			OutlinedTextField(
				value = value.ifBlank { "" },
				onValueChange = onValueChange,
				modifier = Modifier.fillMaxWidth(),
				enabled = isEditable,
				placeholder = {
					if (placeholder.isNotEmpty()) {
						Text(
							text = placeholder,
							style = MaterialTheme.typography.bodyMedium,
							color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
						)
					}
				},
				leadingIcon = if (icon != null) {
					{
						Icon(
							imageVector = icon,
							contentDescription = null,
							tint = if (isEditable) 
								MaterialTheme.colorScheme.primary 
							else 
								MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
						)
					}
				} else null,
				shape = RoundedCornerShape(12.dp),
				singleLine = true,
				colors = OutlinedTextFieldDefaults.colors(
					focusedBorderColor = MaterialTheme.colorScheme.primary,
					unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
					disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
					disabledTextColor = MaterialTheme.colorScheme.onSurface
				)
			)
		}
	}
}