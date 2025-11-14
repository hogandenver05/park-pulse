package com.denverhogan.parkpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.denverhogan.parkpulse.ui.navigation.AppNavigation
import com.denverhogan.parkpulse.ui.theme.ParkPulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkPulseTheme {
                AppNavigation()
            }
        }
    }
}
