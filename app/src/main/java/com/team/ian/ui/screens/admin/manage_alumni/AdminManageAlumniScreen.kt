package com.team.ian.ui.screens.admin.manage_alumni

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
					.horizontalScroll(rememberScrollState())
					.padding(horizontal = 16.dp, vertical = 4.dp),
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				if (selectedStack != null || selectedCountry != null || selectedYear != null) {
					AssistChip(
						onClick = { viewModel.clearAllFilters() },
						label = { Text("Clear All") },
						leadingIcon = {
							Icon(
								Icons.Default.Clear,
								contentDescription = null,
								modifier = Modifier.size(
									18.dp
								)
							)
						}
					)
				}
				stacks.take(5).forEach { stack ->
					FilterChip(
						selected = selectedStack == stack,
						onClick = {
							viewModel.updateStackFilter(if (selectedStack == stack) null else stack)
						},
						label = { Text(stack) }
					)
				}
				countries.take(5).forEach { country ->
					FilterChip(
						selected = selectedCountry == country,
						onClick = {
							viewModel.updateCountryFilter(if (selectedCountry == country) null else country)
						},
						label = { Text(country) }
					)
				}
				years.take(5).forEach { year ->
					FilterChip(
						selected = selectedYear == year,
						onClick = {
							viewModel.updatedYearFilter(if (selectedYear == year) null else year)
						},
						label = { Text(year.toString()) }
					)
				}
			}
			LazyVerticalStaggeredGrid(
				columns = StaggeredGridCells.Fixed(2),
				modifier = Modifier.fillMaxSize(),
				horizontalArrangement = Arrangement.spacedBy(16.dp),
				verticalItemSpacing = 16.dp,
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