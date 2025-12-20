package com.team.ian.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team.ian.ui.screens.home.HomeScreen
import com.team.ian.ui.screens.login.LoginScreen
import com.team.ian.ui.screens.pending.PendingScreen
import com.team.ian.ui.screens.register.RegisterScreen
import com.team.ian.ui.screens.splash.SplashScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash
    ) {
         composable<Screen.Splash> {
            SplashScreen(navController)
        }
        composable<Screen.Home> {
            HomeScreen(navController)
        }
        composable<Screen.Login> {
            LoginScreen(navController)
        }
        composable<Screen.Register> {
            RegisterScreen(navController)
        }
        composable <Screen.Pending>{
            PendingScreen(navController)
        }
    }
}