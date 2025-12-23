package com.team.ian.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.team.ian.ui.screens.admin.AdminDashboard
import com.team.ian.ui.screens.home.HomeScreen
import com.team.ian.ui.screens.login.LoginScreen
import com.team.ian.ui.screens.pending.PendingScreen
import com.team.ian.ui.screens.profile.ProfileScreen
import com.team.ian.ui.screens.register.RegisterScreen
import com.team.ian.ui.screens.splash.SplashScreen
import com.team.ian.ui.screens.utils.FullScreenLoader
import com.team.ian.ui.screens.utils.SnackbarController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
	val navController = rememberNavController()
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val scope = rememberCoroutineScope()

	val items = listOf(
		DrawerItem("Home", Icons.Default.Home, Screen.Home),
		DrawerItem("Profile", Icons.Default.Person, Screen.Profile)
	)

	val snackbarHostState = remember { SnackbarHostState() }

	// Listen for snackbar events
	LaunchedEffect(Unit) {
		SnackbarController.events.collect {
			snackbarHostState.currentSnackbarData?.dismiss()
			snackbarHostState.showSnackbar(
				message = it.msg,
				duration = SnackbarDuration.Long
			)
		}
	}

	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route

	// Screens where the top bar and drawer should NOT appear
	val restrictedScreens = listOf(
		Screen.Login::class.qualifiedName,
		Screen.Splash::class.qualifiedName
	)

	// Only show top bar on Home and Profile
	val showTopbar =
		currentRoute !in restrictedScreens &&
				items.any { it.screen::class.qualifiedName == currentRoute }

	// Close drawer when navigating to restricted screens
	LaunchedEffect(currentRoute) {
		if (currentRoute in restrictedScreens) {
			drawerState.close()
		}
	}

	if (showTopbar) {
		ModalNavigationDrawer(
			drawerState = drawerState,
			gesturesEnabled = true,
			drawerContent = {
				ModalDrawerSheet {
					items.forEach { item ->
						NavigationDrawerItem(
							label = { Text(item.title) },
							selected = item.screen::class.qualifiedName == currentRoute,
							icon = { Icon(item.icon, null) },
							onClick = {
								scope.launch { drawerState.close() }
								navController.navigate(item.screen) {
									launchSingleTop = true
								}
							}
						)
					}
				}
			}
		) {
			Scaffold(
				modifier = Modifier.fillMaxSize(),
				snackbarHost = { SnackbarHost(snackbarHostState) },
				topBar = {
					if (showTopbar) {
						CenterAlignedTopAppBar(
							title = { Text("Ian²") },
							navigationIcon = {
								IconButton(
									onClick = { scope.launch { drawerState.open() } }
								) {
									Icon(Icons.Default.Menu, null)
								}
							}
						)
					}
				}
			) { innerPadding ->
				Box(modifier = Modifier.padding(innerPadding)) {
					FullScreenLoader()
					NavHost(
						navController = navController,
						startDestination = Screen.Register
					) {

						composable<Screen.Splash> {
							SplashScreen(navController)
						}

						composable<Screen.Login> {
							LoginScreen(navController)
						}

						composable<Screen.Home> {
							HomeScreen(navController)
						}

						composable<Screen.Profile> {
							ProfileScreen(navController)
						}

						composable<Screen.Register> {
							RegisterScreen(navController)
						}

						composable<Screen.AdminDashboard> {
							AdminDashboard(navController)
						}
					}
				}
			}
		}
	} else {
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			snackbarHost = { SnackbarHost(snackbarHostState) }
		) { innerPadding ->
			Box(modifier = Modifier.padding(innerPadding)) {
				FullScreenLoader()

				NavHost(
					navController = navController,
					startDestination = Screen.Register
				) {
					composable<Screen.Splash> {
						SplashScreen(navController)
					}

					composable<Screen.Login> {
						LoginScreen(navController)
					}

					composable<Screen.Home> {
						HomeScreen(navController)
					}

					composable<Screen.Profile> {
						ProfileScreen(navController)
					}

					composable<Screen.Register> {
						RegisterScreen(navController)
					}

					composable<Screen.AdminDashboard> {
						AdminDashboard(navController)
					}
				}
			}
		}
	}
}

data class DrawerItem(
	val title: String,
	val icon: ImageVector,
	val screen: Screen
)