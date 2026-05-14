package com.example.habittrackerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habittrackerapp.ui.screens.*

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            SignInScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            DashboardScreen()
        }
        composable(route = Screen.Profile.route) {
            ProfileSettingsScreen(navController = navController)
        }
        composable(route = Screen.CreateHabit.route) {
            CreateHabitScreen(navController = navController)
        }
    }
}
