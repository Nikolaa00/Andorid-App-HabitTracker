package com.example.habittrackerapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Gateway : Screen("gateway_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")
    object Profile : Screen("profile_screen")
    object CreateHabit : Screen("create_habit_screen")
}
