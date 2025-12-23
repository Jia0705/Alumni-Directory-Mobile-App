package com.team.ian.ui.screens.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun AdminViewRegistration(navController: NavController) {
	val viewModel: AdminViewRegistrationViewModel = viewModel()

	LaunchedEffect(Unit) {
//		viewModel.finish.collect {
//			setRefresh(navController)
//			navController.popBackStack()
//		}
	}

	val registration = viewModel.registration.collectAsStateWithLifecycle().value
	if (registration != null) {
		Box(modifier = Modifier.fillMaxSize()) {
			Column() {
				Text("Name: ${registration.name}")
				Text("Email: ${registration.email}")
				Text("Graduation Year: ${registration.gradYear}")
				Text("Department/Major: ${registration.major}")
				Text("Current Position/Job: ${registration.curPosition}")
				Text("Current Company: ${registration.curCompany}")
				Text("Tech Stack: ${registration.techStack}")
				Text("Current city and country: ${registration.curCityandCountry}")
				Text("Contact Links: ${registration.contactLinks}")
			}
		}
		Button(onClick = { /* approve() */ }) {
			Text("Approve")
		}
	}

}