package com.team.ian.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var gradYear by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }
    var curPosition by remember { mutableStateOf("") }
    var curCompany by remember { mutableStateOf("") }
    var techStack by remember { mutableStateOf("") }
    var curCityAndCountry by remember { mutableStateOf("") }
    var contactPref by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var profilePic by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Full name")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Email address")
            OutlinedTextField(
                value = email,
                onValueChange = { email = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Password")
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Graduation year")
            OutlinedTextField(
                value = gradYear,
                onValueChange = { gradYear = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Department/Major")
            OutlinedTextField(
                value = major,
                onValueChange = { major = it }
            )
            Spacer(Modifier.padding(5.dp))

            Text("Current job title/position")
            OutlinedTextField(
                value = curPosition,
                onValueChange = { curPosition = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Current company/organization")
            OutlinedTextField(
                value = curCompany,
                onValueChange = { curCompany = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Primary tech stack / domain")
            OutlinedTextField(
                value = techStack,
                onValueChange = { techStack = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Current city and country")
            OutlinedTextField(
                value = curCityAndCountry,
                onValueChange = { curCityAndCountry = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Contact preference (email, phone, LinkedIn, etc.)")
            OutlinedTextField(
                value = contactPref,
                onValueChange = { contactPref = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Short bio/tagline")
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Profile Picture")
            OutlinedTextField(
                value = profilePic,
                onValueChange = { profilePic = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {}) {
                Text("Register")
            }
        }
    }
}

