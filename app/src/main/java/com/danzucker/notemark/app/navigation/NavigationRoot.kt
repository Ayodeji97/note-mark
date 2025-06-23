package com.danzucker.notemark.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Landing,
        modifier = Modifier
    ) {
        composable<>(NavigationRoute.Landing) {
            LandingScreenRoot(
                onLoginClick = {
                    navController.navigate(NavigationRoute.Login)
                },
                onRegisterClick = {
                    navController.navigate(NavigationRoute.Register)
                }
            )
        }
    }
}