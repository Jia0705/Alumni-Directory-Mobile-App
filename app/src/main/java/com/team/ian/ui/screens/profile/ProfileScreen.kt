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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.ian.ui.components.ProfileSection
import com.team.ian.service.AuthService
import com.team.ian.ui.navigation.Screen
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.ui.components.InfoRow

@Composable
fun ProfileScreen(
	navController: NavController,
	alumniRepo: AlumniRepo = AlumniRepo.getInstance()
) {
    val context = LocalContext.current
    val authService = AuthService.getInstance()
    val user = authService.user.collectAsStateWithLifecycle().value

    val viewModel: ProfileViewModel = viewModel()
    val alumni = viewModel.alumni.collectAsStateWithLifecycle().value

    LaunchedEffect(user?.id) {
        user?.id?.let {
            viewModel.loadAlumniProfile(it)
        }
    }

    fun signOut() {
        authService.signOut()
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
                        .clip(CircleShape)
                ) {
                    if (alumni.photoURL.isNotBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(alumni.photoURL)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Profile photo",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "No profile photo",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Text(
                    text = alumni.fullName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(8.dp))

                ProfileSection(title = "Basic Information", icon = Icons.Filled.Person) {
                    InfoRow("Email", alumni.email, Icons.Filled.Email)
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

                // TODO: Add logic to fetch contact links data
                ProfileSection(title = "Contact Links", icon = Icons.Outlined.Link) {
                    InfoRow("Phone", "+1 (555) 123-4567", Icons.Filled.Phone)
                    InfoRow("LinkedIn", "linkedin.com/in/johndoe", Icons.Outlined.Link)
                    InfoRow("GitHub", "github.com/johndoe", Icons.Outlined.Code)
                }

                // TODO: Add logic to fetch extended info data
                ProfileSection(title = "Extended Information", icon = Icons.Filled.Person) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "About",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Passionate software engineer with 5+ years of experience in mobile development. Love building elegant and efficient solutions.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Skills",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Kotlin • Jetpack Compose • Android • Firebase • REST APIs • Git",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Work Experience",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• Senior Android Developer at Tech Corp (2021-Present)\n• Android Developer at StartupXYZ (2019-2021)\n• Junior Developer at Mobile Solutions Inc (2018-2019)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
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
                    OutlinedButton(
                        onClick = { signOut() },
                        modifier = Modifier
                            .weight(1f)
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
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
