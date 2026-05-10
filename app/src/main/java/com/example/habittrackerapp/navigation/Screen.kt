package com.example.habittrackerapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Profile : Screen("profile")
    object Statistics : Screen("statistics")
    object AddEditHabit : Screen("add_edit_habit?habitId={habitId}") {
        fun createRoute(habitId: String? = null) = "add_edit_habit?habitId=$habitId"
    }
}
