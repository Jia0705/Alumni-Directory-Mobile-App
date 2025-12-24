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
}