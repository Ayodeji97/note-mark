package com.danzucker.notemark.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationRoute {
    @Serializable
    data object Landing : NavigationRoute

    @Serializable
    data object Login : NavigationRoute

    @Serializable
    data object Register : NavigationRoute

    @Serializable
    data object Note : NavigationRoute

    @Serializable
    data class CreateNote(val noteId: String?) : NavigationRoute
}