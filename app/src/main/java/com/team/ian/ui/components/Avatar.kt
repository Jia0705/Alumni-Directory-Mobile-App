package com.team.ian.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.abs

@Composable
fun Avatar(
	name: String,
	modifier: Modifier = Modifier,
	colorName: String? = null
) {
	val initial = name.trim().firstOrNull()?.uppercase() ?: "?"
	val bg = colorName?.let { resolveColor(it) } ?: pickColor(name)

	Box(
		modifier = modifier
			.clip(CircleShape)
			.background(bg),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = initial,
			style = MaterialTheme.typography.displaySmall,
			fontWeight = FontWeight.Bold,
			color = Color.White
		)
	}
}

private fun pickColor(seed: String): Color {
	val palette = listOf(
		Color.Blue,
		Color.Green,
		Color.Red,
		Color.Cyan,
		Color.Magenta,
		Color.Yellow
	)
	val idx = abs(seed.hashCode()) % palette.size
	return palette[idx]
}

private fun resolveColor(value: String): Color {
	return when (value.trim().lowercase()) {
		"blue" -> Color.Blue
		"green" -> Color.Green
		"red" -> Color.Red
		"cyan" -> Color.Cyan
		"magenta" -> Color.Magenta
		"yellow" -> Color.Yellow
		else -> Color.Blue
	}
}
