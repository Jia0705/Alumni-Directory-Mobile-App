package com.team.ian.ui.screens.profile

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.AlumniField
import com.team.ian.ui.components.InfoTextField
import com.team.ian.ui.navigation.Screen
import com.team.ian.ui.screens.utils.setRefresh

@Composable
fun EditOwnProfileScreen(
    navController: NavController
) {
    val viewModel: EditOwnProfileViewModel = viewModel()
    val alumni = viewModel.alumni.collectAsStateWithLifecycle().value
    val extendedInfo = viewModel.extendedInfo.collectAsStateWithLifecycle().value
    var initialAlumni by remember { mutableStateOf(Alumni()) }
    var initialSet by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
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
            text = "Edit Profile",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            text = "Update your professional profile and contact information",
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
            Text(
                text = "These fields cannot be edited",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            InfoTextField(
                label = "Full Name",
                value = alumni.fullName,
                onValueChange = {},
                isEditable = false,
                icon = Icons.Filled.Person
            )
            InfoTextField(
                label = "Email",
                value = alumni.email,
                onValueChange = {},
                isEditable = false,
                icon = Icons.Filled.Email
            )
            InfoTextField(
                label = "Graduation Year",
                value = alumni.graduationYear.toString(),
                onValueChange = {},
                isEditable = false,
                icon = Icons.Filled.School
            )
            InfoTextField(
                label = "Department",
                value = alumni.department,
                onValueChange = {},
                isEditable = false,
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
                text = "Update your work and experience details",
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
                icon = Icons.Filled.Work,
                placeholder = "e.g., Senior Software Engineer"
            )
            InfoTextField(
                label = "Company",
                value = alumni.company,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.COMPANY, it)
                },
                icon = Icons.Filled.Business,
                placeholder = "e.g., Google"
            )
            InfoTextField(
                label = "Tech Stack",
                value = alumni.primaryStack,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.TECH_STACK, it)
                },
                icon = Icons.Filled.Code,
                placeholder = "e.g., React, Node.js, Python"
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
                icon = Icons.Filled.LocationCity,
                placeholder = "e.g., San Francisco"
            )
            InfoTextField(
                label = "Country",
                value = alumni.country,
                onValueChange = {
                    viewModel.updateAlumniField(AlumniField.COUNTRY, it)
                },
                icon = Icons.Filled.Public,
                placeholder = "e.g., United States"
            )

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(
                            Screen.AddOrEditContactLinks(alumni.uid)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Link,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Manage Contact Links")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(
                            Screen.AddOrEditExtendedInfo(alumni.uid)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Manage Extended Info")
                }
            }

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
                        text = "Save Changes",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard changes?") },
            text = { Text("You have unsaved changes.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDiscardDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("Keep Editing")
                }
            }
        )
    }
}