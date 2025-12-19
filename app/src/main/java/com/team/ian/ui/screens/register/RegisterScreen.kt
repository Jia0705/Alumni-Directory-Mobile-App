package com.team.ian.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.team.ian.data.model.RegistrationData
import com.team.ian.data.model.ExtendedInfo

@Composable
fun RegisterScreen(navController: NavController) {
    var pageNumber by remember { mutableStateOf(0) }
    var registrationData by remember {
        mutableStateOf(RegistrationData())
    }
    var extendedInfo by remember {
        mutableStateOf(ExtendedInfo())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (pageNumber) {
                0 -> BasicInfo(
                    registrationData = registrationData,
                    onUpdateData = { registrationData = it },
                    onNext = { pageNumber = 1 }
                )

                1 -> ProfessionalInfo(
                    registrationData = registrationData,
                    onUpdateData = { registrationData = it },
                    onNext = { pageNumber = 2 },
                    onBack = { pageNumber = 0 }
                )

                2 -> LocationInfo(
                    registrationData = registrationData,
                    onUpdateData = { registrationData = it },
                    onNext = { pageNumber = 3 },
                    onBack = { pageNumber = 1 }
                )

                3 -> AdditionalInfo(
                    extendedInfo = extendedInfo,
                    onUpdateData = { extendedInfo = it },
                    onBack = { pageNumber = 2 }
                )
            }
        }
    }
}

@Composable
fun BasicInfo(
    registrationData: RegistrationData,
    onUpdateData: (RegistrationData) -> Unit,
    onNext: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Full name")
            OutlinedTextField(
                value = registrationData.name,
                onValueChange = {
                    onUpdateData(
                        registrationData.copy(name = it)
                    )

                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Email address")
            OutlinedTextField(
                value = registrationData.email,
                onValueChange = {
                    onUpdateData(
                        registrationData.copy(email = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Password")
            OutlinedTextField(
                value = registrationData.pass,
                onValueChange = {
                    onUpdateData(
                        registrationData.copy(pass = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Graduation year")
            OutlinedTextField(
                value = registrationData.gradYear,
                onValueChange = {
                    onUpdateData(
                        registrationData.copy(gradYear = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Department/Major")
            OutlinedTextField(
                value = registrationData.major,
                onValueChange = {
                    onUpdateData(
                        registrationData.copy(major = it)
                    )
                }
            )
            Spacer(Modifier.padding(16.dp))
            Button(onClick = { onNext() }) {
                Text("Next")
            }
        }
    }
}

@Composable
fun ProfessionalInfo(
    registrationData: RegistrationData,
    onUpdateData: (RegistrationData) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.padding(5.dp))
            Text("Current job title/position")
            OutlinedTextField(
                value = registrationData.curPosition,
                onValueChange = {
                    onUpdateData(
                        registrationData.copy(curPosition = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Current company/organization")
            OutlinedTextField(
                value = registrationData.curCompany,
                onValueChange = {
                    onUpdateData(
                        registrationData.copy(curCompany = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Primary tech stack / domain")
            OutlinedTextField(
                value = registrationData.techStack,
                onValueChange = {
                    onUpdateData(
                        registrationData.copy(techStack = it)
                    )
                }
            )
            Spacer(Modifier.padding(16.dp))
            Button(onClick = { onNext() }) {
                Text("Next")
            }
            Button(onClick = { onBack() }) {
                Text("Back")
            }
        }
    }
}

@Composable
fun LocationInfo(
    registrationData: RegistrationData,
    onUpdateData: (RegistrationData) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    Spacer(Modifier.padding(5.dp))
    Text("Current city")
    OutlinedTextField(
        value = city,
        onValueChange = { city = it }
    )
    Spacer(Modifier.padding(5.dp))
    Text("Current country")
    OutlinedTextField(
        value = country,
        onValueChange = { country = it }
    )
    Spacer(Modifier.padding(16.dp))
    Button(onClick = {
        registrationData.copy(curCityandCountry = Pair(city, country))
        onNext()
    }
    ) {
        Text("Next")
    }
    Button(onClick = { onBack() }) {
        Text("Back")
    }
}

@Composable
fun AdditionalInfo(
    extendedInfo: ExtendedInfo,
    onUpdateData: (ExtendedInfo) -> Unit,
    onBack: () -> Unit
) {
//    Text("Past Job History")
//    OutlinedTextField(
//        value = extendedInfo.pastJobHistory,
//        onValueChange = {
//            onUpdateData(
//                extendedInfo.copy(pastJobHistory = it)
//            )
//        }
//    )
//    Spacer(Modifier.padding(5.dp))
//    Text("Skills")
//    OutlinedTextField(
//        value = extendedInfo.skills,
//        onValueChange = {
//            onUpdateData(
//                extendedInfo.copy(skills = it)
//            )
//        }
//    )
    Spacer(Modifier.padding(5.dp))
    Text("Short Bio")
    OutlinedTextField(
        value = extendedInfo.shortBio,
        onValueChange = {
            onUpdateData(
                extendedInfo.copy(shortBio = it)
            )
        }
    )
    Spacer(Modifier.padding(5.dp))
    Text("Profile Picture")
    OutlinedTextField(
        value = extendedInfo.profilePicUrl,
        onValueChange = {
            onUpdateData(
                extendedInfo.copy(profilePicUrl = it)
            )
        }
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = {}) {
        Text("Register")
    }
    Button(onClick = {
        onBack()
    }) {
        Text("Back")
    }
}

