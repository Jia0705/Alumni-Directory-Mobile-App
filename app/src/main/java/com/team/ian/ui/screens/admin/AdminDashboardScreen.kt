package com.team.ian.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.ui.navigation.Screen

@Composable
fun AdminDashboard(navController: NavController) {
	val viewModel: AdminDashboardViewModel = viewModel()
	val registrations = viewModel.registrations.collectAsStateWithLifecycle().value
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(16.dp),
			contentPadding = PaddingValues(16.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			items(registrations) {
				Card(
					elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier
						.fillMaxWidth()
						.clickable {
							navController.navigate(Screen.AdminViewRegistration(it.id))
						}
				) {
					Row(
						modifier = Modifier
							.fillMaxSize()
							.padding(16.dp)
					) {
						Column(modifier = Modifier.fillMaxSize()) {
							Text("${it.name}, ${it.email}")
						}
					}
				}
			}
		}
	}
	if (registrations.isEmpty()) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Text("No registrations")
		}
	}
}