package com.meleha.navcomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meleha.navcomponent.ui.screens.AddItemRoute
import com.meleha.navcomponent.ui.screens.ItemsRoute
import com.meleha.navcomponent.ui.screens.LocalNavController
import com.meleha.navcomponent.ui.screens.add.AddItemScreen
import com.meleha.navcomponent.ui.screens.items.ItemsScreen
import com.meleha.navcomponent.ui.theme.NavigationComponentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationComponentTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavApp()
                }
            }
        }
    }
}

@Composable
fun NavApp() {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = ItemsRoute,
            modifier = Modifier.fillMaxSize(),
        ) {
            composable(ItemsRoute) { ItemsScreen() }
            composable(AddItemRoute) { AddItemScreen() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavAppPreview() {
    NavigationComponentTheme {
        NavApp()
    }
}