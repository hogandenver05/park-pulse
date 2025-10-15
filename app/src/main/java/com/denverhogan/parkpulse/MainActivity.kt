package com.denverhogan.parkpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.denverhogan.parkpulse.list.RidesListScreen
import com.denverhogan.parkpulse.detail.RideDetailScreen
import com.denverhogan.parkpulse.ui.theme.ParkPulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkPulseTheme {
                val navController = rememberNavController()

                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "ridesList",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("ridesList") {
                            RidesListScreen(
                                modifier = Modifier,
                                onRideClick = { ride ->
                                    navController.navigate("rideDetail/${ride.name}")
                                }
                            )
                        }

                        composable(
                            route = "rideDetail/{rideName}",
                            arguments = listOf(navArgument("rideName") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val rideName = backStackEntry.arguments?.getString("rideName") ?: ""
                            RideDetailScreen(rideName, navController = navController,)
                        }
                    }
                }
            }
        }
    }
}
