package com.team.ian.ui.screens.admin.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.data.model.AccountStatus
import com.team.ian.ui.navigation.Screen
import com.team.ian.ui.screens.admin.manage_alumni.AdminManageAlumniViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
	navController: NavController
) {
	val viewModel: AdminDashboardViewModel = viewModel()
	val state = viewModel.state.collectAsStateWithLifecycle().value

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		Text(
			text = "Admin Dashboard",
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.primary
		)

		MetricCard(
			title = "Total Users",
			value = state.totalCount.toString(),
			containerColor = Color(0xFFDBEAFE),
			contentColor = Color(0xFF1E3A8A),
			modifier = Modifier
				.fillMaxWidth()
				.clickable {
					AdminManageAlumniViewModel.presetStatus = null
					navController.navigate(Screen.AdminManageAlumni)
				}
		)

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			MetricCard(
				title = "Approved Alumni",
				value = state.approvedCount.toString(),
				containerColor = Color(0xFFD1FAE5),
				contentColor = Color(0xFF065F46),
				modifier = Modifier
					.weight(1f)
					.clickable {
						AdminManageAlumniViewModel.presetStatus = AccountStatus.APPROVED
						navController.navigate(Screen.AdminManageAlumni)
					}
			)
			MetricCard(
				title = "Pending Registrations",
				value = state.pendingCount.toString(),
				containerColor = Color(0xFFFEF3C7),
				contentColor = Color(0xFF92400E),
				modifier = Modifier
					.weight(1f)
					.clickable {
						navController.navigate(Screen.AdminViewAllRegistrations)
					}
			)
		}

		MetricCard(
			title = "Rejected Alumni",
			value = state.rejectedCount.toString(),
			containerColor = Color(0xFFFEE2E2),
			contentColor = Color(0xFF991B1B),
			modifier = Modifier
				.fillMaxWidth()
				.clickable {
					AdminManageAlumniViewModel.presetStatus = AccountStatus.REJECTED
					navController.navigate(Screen.AdminManageAlumni)
				}
		)

		MetricCard(
			title = "Inactive Alumni",
			value = state.inactiveCount.toString(),
			containerColor = Color(0xFFE5E7EB),
			contentColor = Color(0xFF374151),
			modifier = Modifier
				.fillMaxWidth()
				.clickable {
					AdminManageAlumniViewModel.presetStatus = AccountStatus.INACTIVE
					navController.navigate(Screen.AdminManageAlumni)
				}
		)

		Card(
			modifier = Modifier.fillMaxWidth(),
			elevation = CardDefaults.cardElevation(4.dp),
			shape = RoundedCornerShape(16.dp)
		) {
			Column(modifier = Modifier.padding(16.dp)) {
				Text(
					text = "Recent Approvals",
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold
				)
				Spacer(Modifier.height(8.dp))

				if (state.recentApprovals.isEmpty()) {
					Text(
						text = "No recent approvals yet",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				} else {
					LazyColumn(
						modifier = Modifier.fillMaxWidth(),
						verticalArrangement = Arrangement.spacedBy(8.dp)
					) {
						items(state.recentApprovals) { alumni ->
							Column(
								modifier = Modifier.clickable {
									navController.navigate(Screen.AdminEditAlumniProfile(alumni.uid))
								}
							) {
								Text(
									text = alumni.fullName,
									style = MaterialTheme.typography.bodyLarge,
									fontWeight = FontWeight.Medium
								)
								Text(
									text = "${alumni.company} - ${formatDate(alumni.updatedAt)}",
									style = MaterialTheme.typography.bodySmall,
									color = MaterialTheme.colorScheme.onSurfaceVariant
								)
							}
						}
					}
				}
			}
		}
	}
}

@Composable
private fun MetricCard(
	title: String,
	value: String,
	containerColor: Color,
	contentColor: Color,
	modifier: Modifier = Modifier
) {
	Card(
		modifier = modifier,
		elevation = CardDefaults.cardElevation(4.dp),
		shape = RoundedCornerShape(16.dp),
		colors = CardDefaults.cardColors(containerColor = containerColor)
	) {
		Column(
			modifier = Modifier.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.bodyMedium,
				color = contentColor
			)
			Text(
				text = value,
				style = MaterialTheme.typography.headlineSmall,
				fontWeight = FontWeight.Bold,
				color = contentColor
			)
		}
	}
}

private fun formatDate(timestamp: Long): String {
	if (timestamp <= 0L) return "-"
	val formatter = SimpleDateFormat("MMM d, yyyy", Locale.US)
	return formatter.format(Date(timestamp))
}
