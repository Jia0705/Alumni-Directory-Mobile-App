package com.team.ian.ui.screens.admin

import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun AdminDashboard(navController: NavController) {
	val viewModel: AdminViewModel = viewModel()
	val registrations = viewModel.registrations.collectAsStateWithLifecycle().value
	Box(modifier = Modifier.fillMaxSize()) {
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(16.dp),
			contentPadding = PaddingValues(16.dp),
			modifier = Modifier.fillMaxWidth()
		) {
			items(registrations){
				Card(
					elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier
						.fillMaxWidth().clickable { Log.d("debugging", "this is a registration")}
				) {
					Row(
						modifier = Modifier.fillMaxSize().padding(16.dp)
					) {
						Column(modifier = Modifier.fillMaxSize()) {
							Text("${it.name}, ${it.email}")
						}
					}
				}
			}
		}
	}
}