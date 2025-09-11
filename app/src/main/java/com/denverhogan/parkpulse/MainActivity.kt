package com.denverhogan.parkpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.denverhogan.parkpulse.list.RidesListScreen
import com.denverhogan.parkpulse.ui.theme.ParkPulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkPulseTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()) { innerPadding ->
                    RidesListScreen(
                        modifier = Modifier
                            .padding(innerPadding),
                        onRideClick = {}
                    )
                }
            }
        }
    }
}
