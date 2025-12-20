package com.team.ian.ui.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.team.ian.ui.navigation.Screen

@Composable
fun SplashScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate(Screen.Login) }
            ) {
                Text("go to login screen")
            }
            Button(
                onClick = { navController.navigate(Screen.Home) }
            ) {
                Text("go to home screen")
            }
            Button(
                onClick = { navController.navigate(Screen.Register) }
            ) {
                Text("go to register screen")
            }
            Button(
                onClick = { navController.navigate(Screen.Pending) }
            ) {
                Text("go to pending screen")
            }
        }
    }
}