package com.team.ian.ui.screens.admin.manage_alumni

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import coil.compose.AsyncImage
import com.team.ian.data.model.AccountStatus
import com.team.ian.ui.navigation.Screen

@Composable
fun AdminManageAlumniScreen(
	navController: NavController
) {
	val viewModel: AdminManageAlumniViewModel = viewModel()
	val alumni = viewModel.alumni.collectAsStateWithLifecycle().value
	val search = viewModel.search.collectAsStateWithLifecycle().value
	val selectedStatus = viewModel.selectedStatus.collectAsStateWithLifecycle().value
	val selectedStack = viewModel.selectedStack.collectAsStateWithLifecycle().value
	val selectedCountry = viewModel.selectedCountry.collectAsStateWithLifecycle().value
	val selectedYear = viewModel.selectedYear.collectAsStateWithLifecycle().value
	val context = LocalContext.current

	val stacks = viewModel.availableStacks.collectAsStateWithLifecycle().value
	val countries = viewModel.availableCountries.collectAsStateWithLifecycle().value
	val years = viewModel.availableYears.collectAsStateWithLifecycle().value
	val showFilterDialog = remember { mutableStateOf(false) }
	val tempStack = remember { mutableStateOf<String?>(null) }
	val tempCountry = remember { mutableStateOf<String?>(null) }
	val tempYear = remember { mutableStateOf<Int?>(null) }

	LaunchedEffect(Unit) {
		val preset = AdminManageAlumniViewModel.presetStatus
		if (preset != null) {
			viewModel.updateStatusFilter(preset)
			AdminManageAlumniViewModel.presetStatus = null
		}
	}

	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center,
	) {
		Column(
			modifier = Modifier.fillMaxSize()
		) {
			OutlinedTextField(
				value = search,
				onValueChange = { viewModel.updateSearch(it) },
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				placeholder = { Text("Search alumni...") },
				leadingIcon = {
					Icon(Icons.Default.Search, contentDescription = "Search")
				},
				trailingIcon = {
					if (search.isNotEmpty()) {
						IconButton(onClick = { viewModel.updateSearch("") }) {
							Icon(Icons.Default.Clear, contentDescription = "Clear")
						}
					}
				},
				shape = RoundedCornerShape(12.dp),
				singleLine = true
			)
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.horizontalScroll(rememberScrollState())
					.padding(horizontal = 16.dp, vertical = 4.dp),
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				FilterChip(
					selected = selectedStatus == AccountStatus.APPROVED,
					onClick = {
						viewModel.updateStatusFilter(if (selectedStatus == AccountStatus.APPROVED) null else AccountStatus.APPROVED)
					},
					label = { Text("Approved") }
				)
				FilterChip(
					selected = selectedStatus == AccountStatus.REJECTED,
					onClick = {
						viewModel.updateStatusFilter(if (selectedStatus == AccountStatus.REJECTED) null else AccountStatus.REJECTED)
					},
					label = { Text("Rejected") }
				)
				FilterChip(
					selected = selectedStatus == AccountStatus.INACTIVE,
					onClick = {
						viewModel.updateStatusFilter(if (selectedStatus == AccountStatus.INACTIVE) null else AccountStatus.INACTIVE)
					},
					label = { Text("Inactive") }
				)
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp, vertical = 8.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Row(
					modifier = Modifier
						.weight(1f)
						.horizontalScroll(rememberScrollState()),
					horizontalArrangement = Arrangement.spacedBy(8.dp)
				) {
					if (selectedStack != null) {
						AssistChip(
							onClick = { viewModel.updateStackFilter(null) },
							label = { Text("Stack: $selectedStack") }
						)
					}
					if (selectedCountry != null) {
						AssistChip(
							onClick = { viewModel.updateCountryFilter(null) },
							label = { Text("Country: $selectedCountry") }
						)
					}
					if (selectedYear != null) {
						AssistChip(
							onClick = { viewModel.updatedYearFilter(null) },
							label = { Text("Year: $selectedYear") }
						)
					}
				}

				TextButton(
					onClick = {
						tempStack.value = selectedStack
						tempCountry.value = selectedCountry
						tempYear.value = selectedYear
						showFilterDialog.value = true
					}
				) {
					Icon(Icons.Default.FilterList, contentDescription = null)
					Spacer(Modifier.size(6.dp))
					Text("Filters")
				}
			}
			if (alumni.isEmpty()) {
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = if (search.isEmpty()) "No alumni found" else "No results found",
						style = MaterialTheme.typography.bodyLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
			} else {
				LazyVerticalStaggeredGrid(
					columns = StaggeredGridCells.Fixed(2),
					modifier = Modifier.fillMaxSize(),
					horizontalArrangement = Arrangement.spacedBy(16.dp),
					verticalItemSpacing = 16.dp,
					contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
					content = {
						items(alumni) {
							Card(
								elevation = CardDefaults.cardElevation(4.dp),
								modifier = Modifier.clickable(onClick = {
									navController.navigate(Screen.AdminEditAlumniProfile(it.uid))
								}),
								colors = CardDefaults.cardColors(
									containerColor = when (it.status) {
										AccountStatus.APPROVED -> Color.Green.copy(alpha = 0.2f)
										AccountStatus.INACTIVE -> Color.Gray.copy(alpha = 0.2f)
										AccountStatus.REJECTED -> Color.Red.copy(alpha = 0.2f)
										else -> Color.Black
									}
								)
							) {
								Row(
									modifier = Modifier
										.fillMaxSize()
										.padding(16.dp)
								) {
									Column(
										modifier = Modifier.fillMaxSize(),
										verticalArrangement = Arrangement.spacedBy(5.dp),
										horizontalAlignment = Alignment.CenterHorizontally
									) {
										Box(
											modifier = Modifier
												.fillMaxWidth(0.8f)
												.aspectRatio(1f)
												.clip(CircleShape)
										) {
											if (it.photoURL.isNotBlank()) {
												AsyncImage(
													model = ImageRequest.Builder(context)
														.data(it.photoURL)
														.crossfade(true)
														.build(),
													contentDescription = "Profile photo",
													modifier = Modifier
														.fillMaxSize()
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
														modifier = Modifier.size(36.dp),
														tint = MaterialTheme.colorScheme.surfaceVariant
													)
												}
											}
										}
										Spacer(modifier = Modifier.height(16.dp))
										Text("${it.fullName}, ${it.email}")
									}
								}
							}
						}
					}
				)
			}
		}
	}

	if (showFilterDialog.value) {
		AlertDialog(
			onDismissRequest = { showFilterDialog.value = false },
			title = { Text("Filters") },
			text = {
				Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
					FilterDropdown(
						label = "Tech Stack",
						options = stacks,
						selected = tempStack.value,
						onSelect = { tempStack.value = it }
					)
					FilterDropdown(
						label = "Country",
						options = countries,
						selected = tempCountry.value,
						onSelect = { tempCountry.value = it }
					)
					FilterDropdown(
						label = "Graduation Year",
						options = years.map { it.toString() },
						selected = tempYear.value?.toString(),
						onSelect = { value ->
							tempYear.value = value?.toIntOrNull()
						}
					)
				}
			},
			confirmButton = {
				TextButton(
					onClick = {
						viewModel.updateStackFilter(tempStack.value)
						viewModel.updateCountryFilter(tempCountry.value)
						viewModel.updatedYearFilter(tempYear.value)
						showFilterDialog.value = false
					}
				) {
					Text("Apply")
				}
			},
			dismissButton = {
				TextButton(
					onClick = {
						viewModel.clearAllFilters()
						showFilterDialog.value = false
					}
				) {
					Text("Clear")
				}
			}
		)
	}
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FilterDropdown(
	label: String,
	options: List<String>,
	selected: String?,
	onSelect: (String?) -> Unit
) {
	val expanded = remember { mutableStateOf(false) }
	Column {
		Text(label, style = MaterialTheme.typography.bodyMedium)
		ExposedDropdownMenuBox(
			expanded = expanded.value,
			onExpandedChange = { expanded.value = it }
		) {
			OutlinedTextField(
				value = selected ?: "Any",
				onValueChange = {},
				readOnly = true,
				modifier = Modifier
					.fillMaxWidth()
					.menuAnchor(),
				trailingIcon = {
					ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
				}
			)
			ExposedDropdownMenu(
				expanded = expanded.value,
				onDismissRequest = { expanded.value = false }
			) {
				DropdownMenuItem(
					text = { Text("Any") },
					onClick = {
						onSelect(null)
						expanded.value = false
					}
				)
				options.forEach { option ->
					DropdownMenuItem(
						text = { Text(option) },
						onClick = {
							onSelect(option)
							expanded.value = false
						}
					)
				}
			}
		}
	}
}