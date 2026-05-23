package com.example.habittrackerapp.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habittrackerapp.ui.screens.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittrackerapp.viewmodel.RegisterViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                navController = navController,
                windowSizeClass = windowSizeClass
            )
        }
        composable(route = Screen.Login.route) {
            SignInScreen(
                navController = navController,
                windowSizeClass = windowSizeClass
            )
        }
        composable(route = Screen.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                navController = navController,
                windowSizeClass = windowSizeClass,
                viewModel = viewModel
            )
        }
        composable(route = Screen.Home.route) {
            DashboardScreen(windowSizeClass = windowSizeClass)
        }
        composable(route = Screen.Profile.route) {
            ProfileSettingsScreen(
                navController = navController,
                windowSizeClass = windowSizeClass
            )
        }
        composable(route = Screen.CreateHabit.route) {
            CreateHabitScreen(
                navController = navController,
                windowSizeClass = windowSizeClass
            )
        }
    }
}
