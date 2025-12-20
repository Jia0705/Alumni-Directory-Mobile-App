package com.team.ian.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.team.ian.service.AuthService
import com.team.ian.ui.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController
) {
    val scope = CoroutineScope(Dispatchers.IO)
    val context = LocalContext.current
    val authService = AuthService.getInstance()

    fun signIn() {
        scope.launch {
            authService.signIn(context)
        }
    }

    LaunchedEffect(Unit) {
        authService.user.collect {
            if (it != null) {
                navController.navigate(Screen.Home) {
                    popUpTo(Screen.Login)
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { signIn() }
        ) {
            Text("Sign-in with google")
        }
    }
}