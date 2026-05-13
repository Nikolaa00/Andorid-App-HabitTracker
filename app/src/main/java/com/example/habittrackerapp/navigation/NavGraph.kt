package com.example.habittrackerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            // Splash Screen placeholder
        }
        composable(route = Screen.Gateway.route) {
            // Gateway Screen placeholder
        }
        composable(route = Screen.Login.route) {
            // Login Screen placeholder
        }
        composable(route = Screen.Register.route) {
            // Register Screen placeholder
        }
        composable(route = Screen.Home.route) {
            // Home Screen placeholder
        }
        composable(route = Screen.Profile.route) {
            // Profile Screen placeholder
        }
        composable(route = Screen.CreateHabit.route) {
            // Create Habit Screen placeholder
        }
    }
}
