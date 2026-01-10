package com.team.ian.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
	@Serializable
	object Splash : Screen()
	@Serializable
	object Home : Screen()
	@Serializable
	object Profile : Screen()
	@Serializable
	object EditOwnProfile : Screen()
	@Serializable
	object Settings : Screen()
	@Serializable
	object Login : Screen()
	@Serializable
	object Register : Screen()
	@Serializable
	object Pending : Screen()
	@Serializable
	object Rejected : Screen()
	@Serializable
	object Inactive: Screen()
	@Serializable
	object AdminViewAllRegistrations : Screen()
	@Serializable
	data class AdminViewRegistration(val pendingAlumniId: String) : Screen()
	@Serializable
	object AdminDashboard: Screen()
	@Serializable
	object AdminManageAlumni : Screen()
	@Serializable
	data class AdminEditAlumniProfile(val alumniId: String) : Screen()
	@Serializable
	data class ViewProfile(val alumniId: String) : Screen()
	@Serializable
	object AddOrEditExtendedInfo: Screen()
	@Serializable
	object ExtendedInfo: Screen()
}