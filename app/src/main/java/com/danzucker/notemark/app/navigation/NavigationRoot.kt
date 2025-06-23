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

    }
}