package com.team.ian.ui.screens.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import com.team.ian.ui.components.ProfileSection
import com.team.ian.ui.navigation.Screen
import com.team.ian.ui.components.Avatar
import com.team.ian.ui.components.ExtendedInfoSection
import com.team.ian.ui.components.InfoRow
import com.team.ian.ui.screens.utils.dialPhone
import com.team.ian.ui.screens.utils.openEmail
import com.team.ian.ui.screens.utils.openUrl

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: ProfileViewModel = hiltViewModel()
    val user = viewModel.user.collectAsStateWithLifecycle().value
    val alumni = viewModel.alumni.collectAsStateWithLifecycle().value
    val extendedInfo = viewModel.extendedInfo.collectAsStateWithLifecycle().value
    val contactLinks = viewModel.contactLinks.collectAsStateWithLifecycle().value
    val showColorPicker = remember { mutableStateOf(false) }
    val colorOptions = listOf(
        "blue" to Color.Blue,
        "green" to Color.Green,
        "red" to Color.Red,
        "cyan" to Color.Cyan,
        "magenta" to Color.Magenta,
        "yellow" to Color.Yellow
    )

    fun signOut() {
        viewModel.signOut()
        navController.navigate(Screen.Splash) {
            popUpTo(Screen.Splash) { inclusive = true }
            launchSingleTop = true
        }
    }

    if (user == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "No user logged in",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else if (alumni.uid.isBlank()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 35.dp, bottom = 5.dp),
                text = "My Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                text = "View and manage your profile information",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Photo
                Box(
                    modifier = Modifier
                        .size(140.dp)
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

                TextButton(
                    onClick = { showColorPicker.value = true }
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Edit avatar color")
                }

                ProfileSection(title = "Basic Information", icon = Icons.Filled.Person) {
                    InfoRow(
                        "Email",
                        alumni.email,
                        Icons.Filled.Email,
                        onClick = { openEmail(context, alumni.email) }
                    )
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

                ProfileSection(title = "Contact Links", icon = Icons.Outlined.Link) {
                    InfoRow(
                        "Phone",
                        contactLinks.phoneNumber,
                        Icons.Filled.Phone,
                        onClick = { dialPhone(context, contactLinks.phoneNumber) }
                    )
                    InfoRow(
                        "LinkedIn",
                        contactLinks.linkedIn,
                        Icons.Outlined.Link,
                        onClick = { openUrl(context, contactLinks.linkedIn) }
                    )
                    InfoRow(
                        "GitHub",
                        contactLinks.github,
                        Icons.Outlined.Code,
                        onClick = { openUrl(context, contactLinks.github) }
                    )
                }

                ProfileSection(title = "Extended Information", icon = Icons.Filled.Person) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ExtendedInfoSection(
                            shortBio = extendedInfo.shortBio,
                            skills = extendedInfo.skills,
                            pastJobHistory = extendedInfo.pastJobHistory,
                            showEmptyText = true
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(Screen.EditOwnProfile) },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Edit Profile",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Button(
                        onClick = { navController.navigate(Screen.Settings) },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Privacy Controls",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                OutlinedButton(
                    onClick = { signOut() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showColorPicker.value) {
        AlertDialog(
            onDismissRequest = { showColorPicker.value = false },
            title = { Text("Choose Avatar Color") },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    colorOptions.forEach { (name, color) ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(color, RoundedCornerShape(18.dp))
                                .clickable {
                                    viewModel.updateAvatarColor(name)
                                    showColorPicker.value = false
                                }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showColorPicker.value = false }) {
                    Text("Close")
                }
            }
        )
    }
}