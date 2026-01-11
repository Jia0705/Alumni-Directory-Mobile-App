package com.team.ian.ui.screens.viewProfile

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import android.content.Intent
import com.team.ian.ui.components.Avatar
import com.team.ian.ui.components.InfoRow
import androidx.core.net.toUri

@Composable
fun ViewProfileScreen(
    navController: NavController
) {
    val viewModel: ViewProfileViewModel = hiltViewModel()
    val alumni = viewModel.alumni.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Photo
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .aspectRatio(1f)
                ) {
                    Avatar(
                        name = alumni.fullName,
                        modifier = Modifier.fillMaxSize(),
                        colorName = alumni.avatarColor.ifBlank { null }
                    )
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = alumni.fullName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                // Basic Info
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (alumni.showEmail) {
                        InfoRow("Email", alumni.email,
                            icon = Icons.Default.Email,
                            onClick = { openEmail(context, alumni.email) })
                    }
                    InfoRow(
                        "Graduation Year",
                        alumni.graduationYear.toString(),
                        icon = Icons.Default.Grade
                    )
                    InfoRow(
                        "Department",
                        alumni.department,
                        icon = Icons.Default.LocalFireDepartment
                    )
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                // Professional Info
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoRow("Job Title", alumni.jobTitle, Icons.Filled.Email)
                    InfoRow("Company", alumni.company, Icons.Filled.Email)
                    InfoRow("Tech Stack", alumni.primaryStack, Icons.Filled.Email)
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                // Location
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoRow("City", alumni.city, Icons.Filled.Email)
                    InfoRow("Country", alumni.country, Icons.Filled.Email)
                }

                if (
                    (alumni.showLinkedIn && alumni.linkedin.isNotBlank()) ||
                    (alumni.showGithub && alumni.github.isNotBlank()) ||
                    (alumni.showPhone && alumni.phone.isNotBlank())
                ) {
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))

                    // Contact Methods
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (alumni.showLinkedIn && alumni.linkedin.isNotBlank()) {
                            InfoRow(
                                "LinkedIn",
                                alumni.linkedin,
                                Icons.Filled.Email,
                                onClick = { openUrl(context, alumni.linkedin) }
                            )
                        }
                        if (alumni.showGithub && alumni.github.isNotBlank()) {
                            InfoRow(
                                "GitHub",
                                alumni.github,
                                Icons.Filled.Email,
                                onClick = { openUrl(context, alumni.github) }
                            )
                        }
                        if (alumni.showPhone && alumni.phone.isNotBlank()) {
                            InfoRow(
                                "Phone",
                                alumni.phone,
                                Icons.Filled.Phone,
                                onClick = { dialPhone(context, alumni.phone) }
                            )
                        }
                    }
                }

                if (
                    alumni.shortBio.isNotBlank() ||
                    alumni.skills.isNotEmpty() ||
                    alumni.pastJobHistory.isNotEmpty()
                ) {
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))

                    // Extended Info
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (alumni.shortBio.isNotBlank()) {
                            InfoRow("About", alumni.shortBio, Icons.Filled.Email)
                        }
                        if (alumni.skills.isNotEmpty()) {
                            InfoRow("Skills", alumni.skills.joinToString(", "), Icons.Filled.Email)
                        }
                        if (alumni.pastJobHistory.isNotEmpty()) {
                            InfoRow("Work Experience", alumni.pastJobHistory.joinToString(", "), Icons.Filled.Email)
                        }
                    }
                }
            }
        }
    }
}

private fun openEmail(context: Context, email: String) {
    if (email.isBlank()) return
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:$email".toUri()
    }
    context.startActivity(intent)
}

private fun openUrl(context: Context, rawUrl: String) {
    if (rawUrl.isBlank()) return
    val url = if (rawUrl.startsWith("http")) rawUrl else "https://$rawUrl"
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

private fun dialPhone(context: Context, phone: String) {
    if (phone.isBlank()) return
    val cleaned = phone.replace(" ", "")
    val intent = Intent(Intent.ACTION_DIAL, "tel:$cleaned".toUri())
    context.startActivity(intent)
}
