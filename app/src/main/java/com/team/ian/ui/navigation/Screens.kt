package com.team.ian.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable object Splash: Screen()
    @Serializable object Home: Screen()
    @Serializable object Profile: Screen()
    @Serializable object Login: Screen()
    @Serializable object Register: Screen()
    @Serializable object Pending: Screen()
		@Serializable object Rejected: Screen()
		@Serializable object AdminDashboard: Screen()
		@Serializable data class AdminViewPendingAlumni(val pendingAlumniId: String): Screen()
	@Serializable data class ViewProfile(val alumniId: String): Screen()
}