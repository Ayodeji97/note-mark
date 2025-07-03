package com.danzucker.notemark.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.danzucker.notemark.app.navigation.NavigationRoot
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.isCheckingAuth || !viewModel.state.value.isAuthCheckComplete
            }
        }
        enableEdgeToEdge()
        setContent {
            NoteMarkTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                if (state.isAuthCheckComplete) {
                    NavigationRoot(
                        navController = rememberNavController(),
                        isLoggedIn = state.isLoggedIn
                    )
                }
            }
        }
    }
}