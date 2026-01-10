package com.team.ian.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddOrEditContactMethodsScreen(navController: NavController) {
	var linkedIn by remember { mutableStateOf("") }
	var github by remember { mutableStateOf("") }
	var phoneNumber by remember { mutableStateOf(0) }

	Column(
		Modifier.fillMaxSize()
	) {
		Text(
			modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 5.dp),
			text = "Contact methods",
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.primary,
		)

		Column(
			modifier = Modifier
				.weight(1f)
				.verticalScroll(rememberScrollState())
				.padding(16.dp)
		) {
			OutlinedTextField(
				value = linkedIn,
				onValueChange = {
					linkedIn = it
				}
			)
			OutlinedTextField(
				value = github,
				onValueChange = {
					github = it
				}
			)
			OutlinedTextField(
				value = ,
				onValueChange = { updatedPhoneNumber ->
					if(updatedPhoneNumber.all{it.isDigit()}){
						phoneNumber = updatedPhoneNumber
					}
				}

			)
		}
	}
}