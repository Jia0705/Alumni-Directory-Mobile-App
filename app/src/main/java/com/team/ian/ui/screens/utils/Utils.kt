package com.team.ian.ui.screens.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

// Refresh logic
fun setRefresh(navController: NavController) {
    navController.previousBackStackEntry
        ?.savedStateHandle
        ?.set("refresh", true)
}

suspend fun refreshlistener(navController: NavController, callback: () -> Unit) {
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("refresh", false)
        ?.collect { if (it) callback() }
}

// Snackbar
data class SnackbarEvent(val msg: String)

object SnackbarController {
    private val _events = Channel<SnackbarEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }
}

// Loader
object LoadingManager {
    val isLoading = mutableStateOf(false)

    fun show() { isLoading.value = true }
    fun hide() { isLoading.value = false }
}

@Composable
fun FullScreenLoader(bg: Color = Color(0x88000000)) {
    val isLoading by LoadingManager.isLoading

    if (isLoading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 6.dp,
                    color = Color.Blue
                )
            }
        }
    }
}