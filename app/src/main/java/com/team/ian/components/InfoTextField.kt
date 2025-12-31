package com.team.ian.components

import android.text.Editable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoTextField(
	label: String,
	value: String,
	onValueChange: (String) -> Unit,
	isEditable: Boolean = true
) {
	Column(modifier = Modifier.padding(vertical = 4.dp)) {
		Text(
			text = label,
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		TextField(
			value = value.ifBlank { "" },
			onValueChange = onValueChange,
			modifier = Modifier.fillMaxWidth(),
			enabled = isEditable,
			textStyle = MaterialTheme.typography.bodyLarge,
//			colors = TextFieldDefaults.colors(
//
//			)
		)
	}
}