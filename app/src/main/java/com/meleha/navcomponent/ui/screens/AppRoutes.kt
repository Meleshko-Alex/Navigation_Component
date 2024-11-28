package com.meleha.navcomponent.ui.screens

import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data object ItemsRoute

@Serializable
data object AddItemRoute

@Serializable
data class EditItemRoute(
    val index: Int
)

fun NavBackStackEntry?.routeClass(): KClass<*>? {
    return this?.destination?.route
        ?.split("/")
        ?.first()
        ?.let { Class.forName(it) }
        ?.kotlin
}