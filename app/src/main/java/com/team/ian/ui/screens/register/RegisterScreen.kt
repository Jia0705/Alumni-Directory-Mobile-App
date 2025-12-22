package com.team.ian.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team.ian.data.model.Registration
import com.team.ian.data.model.ExtendedInfo

@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: RegisterViewModel = viewModel()
    var pageNumber by remember { mutableIntStateOf(0) }
    var registration by remember {
        mutableStateOf(Registration())
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
                    registration = registration,
                    onUpdateData = { registration = it },
                    onNext = { pageNumber = 1 }
                )

                1 -> ProfessionalInfo(
                    registration = registration,
                    onUpdateData = { registration = it },
                    onNext = { pageNumber = 2 },
                    onBack = { pageNumber = 0 }
                )

                2 -> LocationInfo(
                    registration = registration,
//                    onUpdateData = { registrationData = it },
//                    onNext = { confirmRegistration() },
                    viewModel = viewModel,
                    onBack = { pageNumber = 1 }
                )

//                3 -> AdditionalInfo(
//                    extendedInfo = extendedInfo,
//                    onUpdateData = { extendedInfo = it },
//                    onBack = { pageNumber = 2 }
//                )

            }
        }
    }
}

@Composable
fun BasicInfo(
    registration: Registration,
    onUpdateData: (Registration) -> Unit,
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
                value = registration.name,
                onValueChange = {
                    onUpdateData(
                        registration.copy(name = it)
                    )

                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Email address")
            OutlinedTextField(
                value = registration.email,
                onValueChange = {
                    onUpdateData(
                        registration.copy(email = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Password")
            OutlinedTextField(
                value = registration.pass,
                onValueChange = {
                    onUpdateData(
                        registration.copy(pass = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Graduation year")
            OutlinedTextField(
                value = registration.gradYear,
                onValueChange = {
                    onUpdateData(
                        registration.copy(gradYear = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Department/Major")
            OutlinedTextField(
                value = registration.major,
                onValueChange = {
                    onUpdateData(
                        registration.copy(major = it)
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
    registration: Registration,
    onUpdateData: (Registration) -> Unit,
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
                value = registration.curPosition,
                onValueChange = {
                    onUpdateData(
                        registration.copy(curPosition = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Current company/organization")
            OutlinedTextField(
                value = registration.curCompany,
                onValueChange = {
                    onUpdateData(
                        registration.copy(curCompany = it)
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
            Text("Primary tech stack / domain")
            OutlinedTextField(
                value = registration.techStack,
                onValueChange = {
                    onUpdateData(
                        registration.copy(techStack = it)
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
    registration: Registration,
    viewModel: RegisterViewModel,
//    onUpdateData: (RegistrationData) -> Unit,
//    onNext: () -> Unit,
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
        registration.copy(curCityandCountry = Pair(city, country))
        viewModel.register(registration)
    }
    ) {
        Text("Register")
    }
    Button(onClick = { onBack() }) {
        Text("Back")
    }
}

//@Composable
//fun AdditionalInfo(
//    extendedInfo: ExtendedInfo,
//    onUpdateData: (ExtendedInfo) -> Unit,
//    onBack: () -> Unit
//) {
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
//    Spacer(Modifier.padding(5.dp))
//    Text("Short Bio")
//    OutlinedTextField(
//        value = extendedInfo.shortBio,
//        onValueChange = {
//            onUpdateData(
//                extendedInfo.copy(shortBio = it)
//            )
//        }
//    )
//    Spacer(Modifier.padding(5.dp))
//    Text("Profile Picture")
//    OutlinedTextField(
//        value = extendedInfo.profilePicUrl,
//        onValueChange = {
//            onUpdateData(
//                extendedInfo.copy(profilePicUrl = it)
//            )
//        }
//    )
//    Spacer(modifier = Modifier.height(16.dp))
//    Button(onClick = {}) {
//        Text("Register")
//    }
//    Button(onClick = {
//        onBack()
//    }) {
//        Text("Back")
//    }
//}

