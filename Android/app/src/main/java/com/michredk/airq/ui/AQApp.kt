package com.michredk.airq.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michredk.metrics.MetricsScreenUiState
import com.michredk.metrics.MetricsScreenUiState.Error
import com.michredk.metrics.MetricsScreenUiState.Success
import com.michredk.metrics.MetricsScreenUiState.Loading
import com.michredk.common.design.AQBackground
import com.michredk.common.design.AQGradientBackground
import com.michredk.metrics.MetricsScreen

@Composable
fun AQApp(
    appState: AQAppState,
    uiState: MetricsScreenUiState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    AQBackground(modifier = modifier) {
        AQGradientBackground(
            top = Color.Red,
            bottom = Color.Blue,
            container = Color.Green
        ) {
            val snackbarHostState = remember { SnackbarHostState() }

            val isOffline by appState.isOffline.collectAsStateWithLifecycle()

            // TODO: If user is not connected to the internet show something.
//            val notConnectedMessage = stringResource(R.string.not_connected)
//            LaunchedEffect(isOffline) {
//                if (isOffline) {
//                    snackbarHostState.showSnackbar(
//                        message = notConnectedMessage,
//                        duration = Indefinite,
//                    )
//                }
//            }

            AQApp(
                appState = appState,
                uiState = uiState,
                snackbarHostState = snackbarHostState,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
    }
}


@Composable
internal fun AQApp(
    appState: AQAppState,
    uiState: MetricsScreenUiState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {

                when (uiState) {
                    is Success -> MetricsScreen(uiState)
                    is Error -> ErrorScreen(message = uiState.message)
                    is Loading -> LoadingScreen()
                }
            }
        }
}

@Composable
fun LoadingScreen() {
    Text("Loading...")
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, message: String) {
    Text("Error Screen: $message")
}
