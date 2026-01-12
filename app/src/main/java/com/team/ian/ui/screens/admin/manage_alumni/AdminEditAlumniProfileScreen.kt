package com.team.ian.ui.screens.admin.manage_alumni

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.team.ian.ui.components.DiscardChangesDialog
import com.team.ian.ui.components.InfoTextField
import com.team.ian.ui.components.ManageInfoCard
import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.AlumniField
import com.team.ian.ui.components.InfoRow
import com.team.ian.ui.components.JobHistoryChips
import com.team.ian.ui.components.SkillsChipRow
import com.team.ian.ui.navigation.Screen
import com.team.ian.data.model.Role
import com.team.ian.ui.screens.utils.dialPhone
import com.team.ian.ui.screens.utils.openUrl
import com.team.ian.ui.screens.utils.setRefresh

@Composable
fun AdminEditAlumniProfileScreen(
    navController: NavController
) {
    val viewModel: AdminEditAlumniProfileViewModel = hiltViewModel()
    val alumni = viewModel.alumni.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    var initialAlumni by remember { mutableStateOf(Alumni()) }
    var initialSet by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    var pendingRole by remember { mutableStateOf<Role?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAlumniById()
        viewModel.getAlumniExtendedInfo()
        viewModel.getAlumniContactLinks()
        viewModel.finish.collect {
            setRefresh(navController)
            navController.popBackStack()
        }
    }

    LaunchedEffect(alumni.uid) {
        if (!initialSet && alumni.uid.isNotBlank()) {
            initialAlumni = alumni
            initialSet = true
        }
    }

    val hasChanges = initialSet && alumni != initialAlumni

    BackHandler {
        if (hasChanges) {
            showDiscardDialog = true
        } else {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 35.dp, bottom = 5.dp),
            text = "Alumni Profile",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            text = "Manage alumni profile information and account status",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Basic Info
            Text(
                text = "Basic Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            InfoTextField(
                label = "Full Name",
                value = alumni.fullName,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.FULL_NAME, it)
                },
                icon = Icons.Filled.Person
            )
            InfoTextField(
                label = "Email",
                value = alumni.email,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.EMAIL, it)
                },
                icon = Icons.Filled.Email
            )
            InfoTextField(
                label = "Graduation Year",
                value = alumni.graduationYear.toString(),
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.GRAD_YEAR, it)
                },
                icon = Icons.Filled.School
            )
            InfoTextField(
                label = "Department",
                value = alumni.department,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.DEPARTMENT, it)
                },
                icon = Icons.Filled.School
            )

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            // Professional Info
            Text(
                text = "Professional Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = "Update work and experience details",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            InfoTextField(
                label = "Job Title",
                value = alumni.jobTitle,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.JOB_TITLE, it)
                },
                icon = Icons.Filled.Work
            )
            InfoTextField(
                label = "Company",
                value = alumni.company,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.COMPANY, it)
                },
                icon = Icons.Filled.Business
            )
            InfoTextField(
                label = "Tech Stack",
                value = alumni.primaryStack,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.TECH_STACK, it)
                },
                icon = Icons.Filled.Code
            )

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            // Location
            Text(
                text = "Location",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = "Where are you currently based?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            InfoTextField(
                label = "City",
                value = alumni.city,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.CITY, it)
                },
                icon = Icons.Filled.LocationCity
            )
            InfoTextField(
                label = "Country",
                value = alumni.country,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.COUNTRY, it)
                },
                icon = Icons.Filled.Public
            )

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Contact Methods",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = "Professional contact information",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (alumni.linkedin.isNotBlank() || alumni.github.isNotBlank() || alumni.phone.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (alumni.linkedin.isNotBlank()) {
                            InfoRow("LinkedIn", alumni.linkedin, Icons.Outlined.Link, onClick = { openUrl(context, alumni.linkedin) })
                        }
                        if (alumni.github.isNotBlank()) {
                            InfoRow("GitHub", alumni.github, Icons.Outlined.Link, onClick = { openUrl(context, alumni.github) })
                        }
                        if (alumni.phone.isNotBlank()) {
                            InfoRow("Phone", alumni.phone, Icons.Filled.Phone, onClick = { dialPhone(context, alumni.phone) })
                        }
                    }
                }
            } else {
                Text(
                    text = "No contact methods added yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Extended Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = "Bio, skills, and work history",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // About Section
            if (alumni.shortBio.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "About",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            alumni.shortBio,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Skills Section
            if (alumni.skills.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Skills",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        val skills = alumni.skills.filter { it.isNotBlank() }
                        if (skills.isEmpty()) {
                            Text(
                                text = "No skills added yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        } else {
                            SkillsChipRow(skills = skills)
                        }
                    }
                }
            }

            // Work Experience Section
            if (alumni.pastJobHistory.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Work Experience",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        val jobs = alumni.pastJobHistory.filter { it.isNotBlank() }
                        if (jobs.isEmpty()) {
                            Text(
                                text = "No work experience added yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        } else {
                            JobHistoryChips(jobs = jobs)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Account Status",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = "Manage user account status",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.updateAlumniStatusState(AccountStatus.APPROVED)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(
                            if (alumni.status == AccountStatus.APPROVED) {
                                Color.Cyan.copy(0.4f)
                            } else {
                                Color.Transparent
                            }
                        ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Approved")
                }
                OutlinedButton(
                    onClick = {
                        viewModel.updateAlumniStatusState(AccountStatus.REJECTED)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(
                            if (alumni.status == AccountStatus.REJECTED) {
                                Color.Cyan.copy(0.4f)
                            } else {
                                Color.Transparent
                            }
                        ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Rejected")
                }
                OutlinedButton(
                    onClick = {
                        viewModel.updateAlumniStatusState(AccountStatus.INACTIVE)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(
                            if (alumni.status == AccountStatus.INACTIVE) {
                                Color.Cyan.copy(0.4f)
                            } else {
                                Color.Transparent
                            }
                        ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Inactive")
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Account Role",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = "Manage user role permissions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (alumni.role == Role.ALUMNI) {
                    Button(
                        onClick = { pendingRole = Role.ADMIN },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Promote to Admin")
                    }
                } else {
                    Button(
                        onClick = { pendingRole = Role.ALUMNI },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Demote to Alumni")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Extended Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

            ManageInfoCard(
                label = "Manage Contact Links",
                icon = Icons.Outlined.Link,
                onClick = {
                    navController.navigate(
                        Screen.AddOrEditContactLinks(alumni.uid)
                    )
                }
            )

            ManageInfoCard(
                label = "Manage Extended Info",
                icon = Icons.Outlined.Info,
                onClick = {
                    navController.navigate(
                        Screen.AddOrEditExtendedInfo(alumni.uid)
                    )
                }
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.resetAllStates()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Reset",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Button(
                    onClick = {
                        viewModel.finishEditing()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Spacer(Modifier.height(16.dp))
        }
    }

    if (showDiscardDialog) {
		DiscardChangesDialog(
			onDiscard = {
				showDiscardDialog = false
				navController.popBackStack()
			},
			onDismiss = { showDiscardDialog = false }
		)
	}

	if (pendingRole != null) {
		val nextRole = pendingRole ?: Role.ALUMNI
		AlertDialog(
			onDismissRequest = { pendingRole = null },
			title = { Text("Change role?") },
			text = {
				Text(
					text = "Are you sure you want to set this user to ${nextRole.name.lowercase()}?"
				)
			},
			confirmButton = {
				TextButton(
					onClick = {
						viewModel.updateAlumniRole(nextRole)
						pendingRole = null
					}
				) {
					Text("Confirm")
				}
			},
			dismissButton = {
				TextButton(onClick = { pendingRole = null }) {
					Text("Cancel")
				}
			}
		)
	}
}