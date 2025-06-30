package com.danzucker.notemark.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.danzucker.notemark.auth.presentation.landing.LandingScreen
import com.danzucker.notemark.auth.presentation.login.LoginRoot
import com.danzucker.notemark.auth.presentation.register.RegisterRoot
import com.danzucker.notemark.note.presentation.createnote.CreateNoteRoot
import com.danzucker.notemark.note.presentation.notelist.NoteRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Landing,
    ) {
        composable<NavigationRoute.Landing> {
            LandingScreen(
                onGetStartedClick = {
                    navController.navigate(NavigationRoute.Register) {
                        popUpTo(NavigationRoute.Landing) {
                            inclusive = true
                        }
                    }
                },
                onLoginInClick = {
                    navController.navigate(NavigationRoute.Login) {
                        popUpTo(NavigationRoute.Landing) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<NavigationRoute.Login> {
            LoginRoot(
                onLoginSuccess = {
                    navController.navigate(NavigationRoute.Note) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(NavigationRoute.Register) {
                        popUpTo(NavigationRoute.Login) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<NavigationRoute.Register> {
            RegisterRoot(
                onRegisterSuccess = {
                    navController.navigate(NavigationRoute.Login) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }

                },
                onNavigateToLogin = {
                    navController.navigate(NavigationRoute.Login) {
                        popUpTo(NavigationRoute.Register) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<NavigationRoute.Note> {
            NoteRoot(
                onNavigateToCreateNote = {
                    navController.navigate(NavigationRoute.CreateNote)
                }
            )
        }

        composable<NavigationRoute.CreateNote> {
            CreateNoteRoot(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}