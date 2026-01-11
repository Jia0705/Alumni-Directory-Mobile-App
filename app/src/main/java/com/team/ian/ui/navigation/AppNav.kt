package com.team.ian.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.team.ian.ui.screens.home.HomeScreen
import com.team.ian.ui.screens.login.LoginScreen
import com.team.ian.ui.screens.profile.EditOwnProfileScreen
import com.team.ian.ui.screens.profile.ProfileScreen
import com.team.ian.ui.screens.settings.SettingsScreen
import com.team.ian.ui.screens.register.RegisterScreen
import com.team.ian.ui.screens.splash.SplashScreen
import com.team.ian.ui.screens.status.PendingScreen
import com.team.ian.ui.screens.status.RejectedScreen
import com.team.ian.ui.screens.status.InactiveScreen
import com.team.ian.ui.screens.utils.FullScreenLoader
import com.team.ian.ui.screens.utils.SnackbarController
import com.team.ian.service.AuthService
import com.team.ian.data.model.Role
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.ui.screens.admin.manage_alumni.AdminEditAlumniProfileScreen
import com.team.ian.ui.screens.admin.manage_alumni.AdminManageAlumniScreen
import com.team.ian.ui.screens.admin.dashboard.AdminDashboardScreen
import com.team.ian.ui.screens.admin.registrations.AdminViewAllRegistrationsScreen
import com.team.ian.ui.screens.admin.registrations.AdminViewRegistrationScreen
import com.team.ian.ui.screens.profile.AddOrEditContactLinksScreen
import com.team.ian.ui.screens.profile.AddOrEditExtendedInfoScreen
import com.team.ian.ui.screens.viewProfile.ViewProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
	val navController = rememberNavController()
	val drawerState = rememberDrawerState(DrawerValue.Closed)
	val scope = rememberCoroutineScope()
	val authService = AuthService.getInstance()
	val alumniRepo = AlumniRepo.getInstance()
	val currentUser = authService.user.collectAsStateWithLifecycle().value
	val pendingAlumni = alumniRepo.getPendingAlumni().collectAsStateWithLifecycle(
		initialValue = emptyList()
	).value
	val pendingCount = pendingAlumni.size

	val allDrawerItems = listOf(
		DrawerItem("Home", Icons.Default.Home, Screen.Home),
		DrawerItem("Admin Dashboard", Icons.Default.Dashboard, Screen.AdminDashboard, Role.ADMIN),
		DrawerItem("Manage Alumni", Icons.Default.People, Screen.AdminManageAlumni, Role.ADMIN),
		DrawerItem("View Registrations", Icons.Default.Dashboard, Screen.AdminViewAllRegistrations, Role.ADMIN),
		DrawerItem("Profile", Icons.Default.Person, Screen.Profile)
	)

	// Filter drawer items based on user role
	val drawerItems = allDrawerItems.filter { item ->
		item.requiredRole == null || currentUser?.role == item.requiredRole
	}

	val snackbarHostState = remember { SnackbarHostState() }

	// Listen for snackbar events
	LaunchedEffect(Unit) {
		SnackbarController.events.collect {
			snackbarHostState.currentSnackbarData?.dismiss()
			snackbarHostState.showSnackbar(
				message = it.msg,
				actionLabel = null,
				duration = SnackbarDuration.Long
			)
		}
	}

	val backStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = backStackEntry?.destination?.route

	val noScaffoldScreens = listOf(
		Screen.Splash::class.qualifiedName,
		Screen.Login::class.qualifiedName,
		Screen.Register::class.qualifiedName,
		Screen.Pending::class.qualifiedName,
		Screen.Rejected::class.qualifiedName,
		Screen.Inactive::class.qualifiedName,
		Screen.AdminViewRegistration::class.qualifiedName,
    	Screen.AdminEditAlumniProfile::class.qualifiedName,
    	Screen.ViewProfile::class.qualifiedName
	)

	val showScaffold =
		currentRoute !in noScaffoldScreens &&
				drawerItems.any { it.screen::class.qualifiedName == currentRoute }

	if (showScaffold) {
		ModalNavigationDrawer(
			drawerState = drawerState,
			gesturesEnabled = true,
			drawerContent = {
				ModalDrawerSheet {
					drawerItems.forEach { item ->
						NavigationDrawerItem(
							label = {
								if (item.screen == Screen.AdminViewAllRegistrations) {
									Row(modifier = Modifier.fillMaxWidth()) {
										Text(
											text = item.title,
											modifier = Modifier.weight(1f)
										)
										Text(
											text = pendingCount.toString(),
											textAlign = TextAlign.End,
											color = MaterialTheme.colorScheme.onSurfaceVariant
										)
									}
								} else {
									Text(item.title)
								}
							},
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
					Spacer(Modifier.weight(1f))
				}
			}
		) {
			Scaffold(
				snackbarHost = { SnackbarHost(snackbarHostState) },
				topBar = {
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
			) { padding ->
				Box(Modifier.padding(padding)) {
					NavHost(
						navController = navController,
						startDestination = Screen.Splash
					) {

						composable<Screen.Splash> {
							SplashScreen(navController)
						}

						composable<Screen.Login> {
							LoginScreen(navController)
						}

						composable<Screen.Register> {
							RegisterScreen(navController)
						}

						composable<Screen.Pending> {
							PendingScreen(navController)
						}

						composable<Screen.Rejected> {
							RejectedScreen(navController)
						}

						composable<Screen.Inactive> {
							InactiveScreen(navController)
						}

						composable<Screen.AdminManageAlumni> {
							AdminManageAlumniScreen(navController)
						}

						composable<Screen.Home> {
							HomeScreen(navController)
						}

						composable<Screen.Profile> {
							ProfileScreen(navController)
						}

						composable<Screen.EditOwnProfile> {
							EditOwnProfileScreen(navController)
						}

						composable<Screen.Settings> {
							SettingsScreen(navController)
						}

						composable<Screen.AdminViewAllRegistrations> {
							AdminViewAllRegistrationsScreen(navController)
						}

						composable<Screen.AdminDashboard> {
							AdminDashboardScreen(navController)
						}

						composable<Screen.AdminViewRegistration> {
							AdminViewRegistrationScreen(navController)
						}

						composable<Screen.AdminEditAlumniProfile> {
							AdminEditAlumniProfileScreen(navController)
						}

						composable<Screen.ViewProfile> {
							ViewProfileScreen(navController)
						}

						composable<Screen.AddOrEditExtendedInfo> {
							AddOrEditExtendedInfoScreen(navController)
						}

                        composable<Screen.AddOrEditContactLinks> {
                            AddOrEditContactLinksScreen(navController)
                        }
					}

					FullScreenLoader()
				}
			}
		}
	} else {
		// Screens without scaffold
		Box(Modifier.fillMaxSize()) {
			NavHost(
				navController = navController,
				startDestination = Screen.Splash
			) {

				composable<Screen.Splash> {
					SplashScreen(navController)
				}

				composable<Screen.Login> {
					LoginScreen(navController)
				}

				composable<Screen.Register> {
					RegisterScreen(navController)
				}

				composable<Screen.Pending> {
					PendingScreen(navController)
				}

				composable<Screen.AdminManageAlumni> {
					AdminManageAlumniScreen(navController)
				}

				composable<Screen.Rejected> {
					RejectedScreen(navController)
				}

				composable<Screen.Inactive> {
					InactiveScreen(navController)
				}

				composable<Screen.Home> {
					HomeScreen(navController)
				}

				composable<Screen.Profile> {
					ProfileScreen(navController)
				}

				composable<Screen.EditOwnProfile> {
					EditOwnProfileScreen(navController)
				}

				composable<Screen.Settings> {
					SettingsScreen(navController)
				}

				composable<Screen.AdminViewAllRegistrations> {
					AdminViewAllRegistrationsScreen(navController)
				}

				composable<Screen.AdminDashboard> {
					AdminDashboardScreen(navController)
				}

				composable<Screen.AdminViewRegistration> {
					AdminViewRegistrationScreen(navController)
				}

				composable<Screen.AdminEditAlumniProfile> {
					AdminEditAlumniProfileScreen(navController)
				}

				composable<Screen.ViewProfile> {
					ViewProfileScreen(navController)
				}

				composable<Screen.AddOrEditExtendedInfo> {
					AddOrEditExtendedInfoScreen(navController)
				}

                composable<Screen.AddOrEditContactLinks> {
                    AddOrEditContactLinksScreen(navController)
                }
			}

			FullScreenLoader()
		}
	}
}

data class DrawerItem(
	val title: String,
	val icon: ImageVector,
	val screen: Screen,
	val requiredRole: Role? = null
)
