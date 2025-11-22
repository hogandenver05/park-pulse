package com.denverhogan.parkpulse.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.denverhogan.parkpulse.ui.parks.ParksListScreen
import com.denverhogan.parkpulse.ui.rides.RidesListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "parks",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("parks") {
                ParksListScreen(navController = navController)
            }
            composable(
                route = "parks/{parkId}/rides/{parkName}",
                arguments = listOf(
                    navArgument("parkId") { type = NavType.IntType },
                    navArgument("parkName") { type = NavType.StringType }
                )
            ) {
                RidesListScreen()
            }
        }
    }
}
