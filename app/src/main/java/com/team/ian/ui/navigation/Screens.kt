package com.team.ian.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable object Home: Screen()
    @Serializable object Profile: Screen()
    @Serializable object Login: Screen()
    @Serializable object Splash: Screen()
}