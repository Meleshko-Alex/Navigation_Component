package com.meleha.navcomponent.ui.screens

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data object ItemsGraph {
    @Serializable
    data object ItemsRoute

    @Serializable
    data object AddItemRoute

    @Serializable
    data class EditItemRoute(
        val index: Int
    ) {
        companion object {
            val Link = navDeepLink {
                uriPattern = "nav://items/{index}"
            }
        }
    }
}

@Serializable
data object SettingsGraph {

    val Link = navDeepLink {
        uriPattern = "nav://settings"
    }

    @Serializable
    data object SettingsRoute
}

@Serializable
data object ProfileGraph {
    @Serializable
    data object ProfileRoute
}

fun NavBackStackEntry?.routeClass(): KClass<*>? {
    return this?.destination.routeClass()
}

fun NavDestination?.routeClass(): KClass<*>? {
    return this?.route
        ?.split("/")
        ?.first()
        ?.let { className ->
            generateSequence(className, ::replaceLastDotByDollar)
                .mapNotNull(::tryParseClass)
                .firstOrNull()
        }
}

private fun tryParseClass(className: String): KClass<*>? {
    return runCatching { Class.forName(className).kotlin }.getOrNull()
}

private fun replaceLastDotByDollar(input: String): String? {
    val index = input.lastIndexOf('.')
    return if (index != -1) {
        String(input.toCharArray().apply { set(index, '$') })
    } else {
        null
    }
}