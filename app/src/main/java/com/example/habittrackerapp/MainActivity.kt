package com.example.habittrackerapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerapp.data.repository.HabitRepository
import com.example.habittrackerapp.navigation.Screen
import com.example.habittrackerapp.ui.MainNavigationShell
import com.example.habittrackerapp.ui.theme.HabitTrackerAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    @Inject
    lateinit var repository: HabitRepository

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission result handled
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        askNotificationPermission()
        setContent {
            HabitTrackerAppTheme {
                val isLoaded by repository.isSessionLoaded.collectAsStateWithLifecycle()
                val userSession by repository.userSession.collectAsStateWithLifecycle()
                
                // Show Splash Screen while session is loading from DB
                val startDestination = if (!isLoaded) {
                    Screen.Splash.route
                } else if (userSession == null) {
                    Screen.Welcome.route
                } else {
                    Screen.Home.route
                }

                val navController = rememberNavController()
                val windowSizeClass = calculateWindowSizeClass(this)
                
                MainNavigationShell(
                    navController = navController,
                    windowSizeClass = windowSizeClass,
                    startDestination = startDestination
                )
            }
        }
    }
}
