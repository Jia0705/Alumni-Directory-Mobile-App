package com.team.ian.ui.screens.admin

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.team.ian.ui.screens.utils.setRefresh

@Composable
fun AdminViewPendingAlumniScreen(navController: NavController) {
	val viewModel: AdminViewPendingAlumniViewModel = hiltViewModel()

	LaunchedEffect(Unit) {
		Log.d("debugging", "launch effect")
		viewModel.finish.collect {
			Log.d("debugging", "approved")
			setRefresh(navController)
			navController.popBackStack()
		}
	}

	AdminViewPendingAlumni(viewModel)

}

@Composable
fun AdminViewPendingAlumni(viewModel: AdminViewPendingAlumniViewModel) {
	val alumni = viewModel.pendingAlumni.collectAsStateWithLifecycle().value
	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
		,
		Alignment.Center
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(5.dp)
		) {
			if (alumni != null) {
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
				Spacer(modifier = Modifier.height(16.dp))
				Button(
					onClick = {
						Log.d("button","button")
						val uid = alumni.uid
						viewModel.approveAlumni(uid)
						Log.d("debugging", "button clicked")
					}
				) {
					Text("Approve alumni registration ")
				}
			} else {
				Text("An error occurred")
			}
		}
	}
}
