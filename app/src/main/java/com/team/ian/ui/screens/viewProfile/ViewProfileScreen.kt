package com.team.ian.ui.screens.viewProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Link
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
import com.team.ian.ui.components.Avatar
import com.team.ian.ui.components.ExtendedInfoSection
import com.team.ian.ui.components.InfoRow
import com.team.ian.ui.components.ProfileSection
import com.team.ian.ui.screens.utils.dialPhone
import com.team.ian.ui.screens.utils.openEmail
import com.team.ian.ui.screens.utils.openUrl

@Composable
fun ViewProfileScreen(
    navController: NavController
) {
    val viewModel: ViewProfileViewModel = hiltViewModel()
    val alumni = viewModel.alumni.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(64.dp))

        // Profile Photo
        Box(
            modifier = Modifier.size(140.dp)
        ) {
            Avatar(
                name = alumni.fullName,
                modifier = Modifier.fillMaxSize(),
                colorName = alumni.avatarColor.ifBlank { null }
            )
        }

        Text(
            text = alumni.fullName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(8.dp))

        ProfileSection(title = "Basic Information", icon = Icons.Filled.Person) {
            if (alumni.showEmail) {
                InfoRow(
                    "Email",
                    alumni.email,
                    Icons.Filled.Email,
                    onClick = { openEmail(context, alumni.email) }
                )
            }
            InfoRow(
                "Graduation Year",
                alumni.graduationYear.toString(),
                Icons.Filled.School
            )
            InfoRow("Department", alumni.department, Icons.Filled.School)
        }

        ProfileSection(title = "Professional Information", icon = Icons.Filled.Work) {
            InfoRow("Job Title", alumni.jobTitle, Icons.Filled.Work)
            InfoRow("Company", alumni.company, Icons.Filled.Business)
            InfoRow("Tech Stack", alumni.primaryStack, Icons.Filled.Code)
        }

        ProfileSection(title = "Location", icon = Icons.Filled.LocationOn) {
            InfoRow("City", alumni.city, Icons.Filled.LocationCity)
            InfoRow("Country", alumni.country, Icons.Filled.Public)
        }

        if ((alumni.showLinkedIn && alumni.linkedin.isNotBlank()) ||
            (alumni.showGithub && alumni.github.isNotBlank()) ||
            (alumni.showPhone && alumni.phone.isNotBlank())
        ) {
            ProfileSection(title = "Contact Links", icon = Icons.Outlined.Link) {
                if (alumni.showPhone && alumni.phone.isNotBlank()) {
                    InfoRow(
                        "Phone",
                        alumni.phone,
                        Icons.Filled.Phone,
                        onClick = { dialPhone(context, alumni.phone) }
                    )
                }
                if (alumni.showLinkedIn && alumni.linkedin.isNotBlank()) {
                    InfoRow(
                        "LinkedIn",
                        alumni.linkedin,
                        Icons.Outlined.Link,
                        onClick = { openUrl(context, alumni.linkedin) }
                    )
                }
                if (alumni.showGithub && alumni.github.isNotBlank()) {
                    InfoRow(
                        "GitHub",
                        alumni.github,
                        Icons.Outlined.Code,
                        onClick = { openUrl(context, alumni.github) }
                    )
                }
            }
        }

        if (alumni.shortBio.isNotBlank() ||
            alumni.skills.isNotEmpty() ||
            alumni.pastJobHistory.isNotEmpty()
        ) {
            ProfileSection(title = "Extended Information", icon = Icons.Filled.Person) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ExtendedInfoSection(
                        shortBio = alumni.shortBio,
                        skills = alumni.skills,
                        pastJobHistory = alumni.pastJobHistory,
                        showEmptyText = true
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
