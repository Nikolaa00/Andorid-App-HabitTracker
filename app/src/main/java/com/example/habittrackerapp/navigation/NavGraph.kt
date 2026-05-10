package com.example.habittrackerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.habittrackerapp.ui.screens.auth.LoginScreen
import com.example.habittrackerapp.ui.screens.auth.RegisterScreen
import com.example.habittrackerapp.ui.screens.auth.WelcomeScreen
import com.example.habittrackerapp.ui.screens.dashboard.DashboardScreen
import com.example.habittrackerapp.ui.screens.dashboard.HabitItemData
import com.example.habittrackerapp.ui.screens.habits.AddEditHabitScreen
import com.example.habittrackerapp.ui.screens.profile.ProfileScreen
import com.example.habittrackerapp.ui.screens.splash.SplashScreen
import com.example.habittrackerapp.ui.screens.statistics.StatisticsScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onGuestClick = { navController.navigate(Screen.Dashboard.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onSignInClick = { _, _ -> navController.navigate(Screen.Dashboard.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onForgotPasswordClick = {}
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = { _, _, _ -> navController.navigate(Screen.Dashboard.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onGoogleClick = {},
                onFacebookClick = {}
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                userName = "John Doe",
                streakDays = 12,
                completedHabits = 4,
                totalHabits = 6,
                habits = listOf(
                    HabitItemData("1", "Hydration Goal", "2.5L / 3L", false),
                    HabitItemData("2", "Mindfulness", "15 min Session", true),
                    HabitItemData("3", "Evening Walk", "30 min at 6 PM", false)
                ),
                onHabitClick = {},
                onHabitToggle = {}
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                userName = "John Doe",
                memberSinceYear = 2023,
                streakDays = 12,
                totalCompletions = 482,
                notificationsEnabled = true,
                onNotificationsToggle = {},
                onLogoutClick = { navController.navigate(Screen.Welcome.route) }
            )
        }
        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }
        composable(
            route = Screen.AddEditHabit.route,
            arguments = listOf(navArgument("habitId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")
            AddEditHabitScreen(
                habitId = habitId,
                onSaveClick = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
