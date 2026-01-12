package com.team.ian.ui.screens.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun openEmail(context: Context, email: String) {
	if (email.isBlank()) return
	val intent = Intent(Intent.ACTION_SENDTO).apply {
		data = "mailto:$email".toUri()
	}
	context.startActivity(intent)
}

fun openUrl(context: Context, rawUrl: String) {
	if (rawUrl.isBlank()) return
	val url = if (rawUrl.startsWith("http")) rawUrl else "https://$rawUrl"
	val intent = Intent(Intent.ACTION_VIEW, url.toUri())
	context.startActivity(intent)
}

fun dialPhone(context: Context, phone: String) {
	if (phone.isBlank()) return
	val cleaned = phone.replace(" ", "")
	val intent = Intent(Intent.ACTION_DIAL, "tel:$cleaned".toUri())
	context.startActivity(intent)
}
