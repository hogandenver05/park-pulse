package com.denverhogan.parkpulse

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.denverhogan.parkpulse.ui.navigation.AppNavigation
import com.denverhogan.parkpulse.ui.theme.ParkPulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission result is handled by LocationProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Request location permission on app start
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        
        setContent {
            ParkPulseTheme {
                AppNavigation()
            }
        }
    }
}
