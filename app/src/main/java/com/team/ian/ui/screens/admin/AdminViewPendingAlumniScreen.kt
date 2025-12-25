package com.team.ian.ui.screens.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun AdminViewPendingAlumniScreen(navController: NavController) {
	val viewModel: AdminViewPendingAlumniViewModel = hiltViewModel()

	fun approve(){
	}

//	LaunchedEffect(Unit) {
////		viewModel.finish.collect {
////			setRefresh(navController)
////			navController.popBackStack()
////		}
//	}

	val alumni = viewModel.pendingAlumni.collectAsStateWithLifecycle().value
	Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
		Column(
			modifier = Modifier.fillMaxSize()
		) {
			Text("Full Name: ${alumni.fullName}")
			Text("Email: ${alumni.email}")
			Text("Graduation Year: ${alumni.graduationYear}")
			Text("Department/Major: ${alumni.department}")
			Text("Current Position/Job: ${alumni.jobTitle}")
			Text("Current Company: ${alumni.company}")
			Text("Tech Stack: ${alumni.primaryStack}")
			Text("Current city: ${alumni.city}")
			Text("Current country: ${alumni.country}")
			Text("Contact Links: ${alumni.linkedin}, ${alumni.github}, ${alumni.phone}, ${alumni.photoURL}")
		}
		Button(onClick = {
			approve()
		}) {
			Text("Approve")
		}
	}

}