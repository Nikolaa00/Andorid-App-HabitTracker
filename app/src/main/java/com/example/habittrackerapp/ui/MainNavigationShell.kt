package com.example.habittrackerapp.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.habittrackerapp.R
import com.example.habittrackerapp.navigation.Screen
import com.example.habittrackerapp.navigation.SetupNavGraph
import com.example.habittrackerapp.ui.components.LanguageSwitcherBar
import com.example.habittrackerapp.util.LocaleHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationShell(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    startDestination: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentLanguage = rememberSaveable { mutableStateOf(LocaleHelper.getSelectedLanguage()) }

    val isTablet = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val bottomBarScreens = listOf(Screen.Home.route, Screen.Profile.route, Screen.CreateHabit.route)
    val showNavigation = currentDestination?.route in bottomBarScreens

    Scaffold(
        topBar = {
            if (showNavigation) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        LanguageSwitcherBar(
                            currentLanguage = currentLanguage.value,
                            onLanguageChange = { lang ->
                                LocaleHelper.setLocale(lang)
                                currentLanguage.value = lang
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags(lang)
                                )
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        },
        bottomBar = {
            if (showNavigation && !isTablet) {
                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    val navItems = listOf(
                        Triple(Screen.Home, Icons.Default.Home, R.string.home_nav),
                        Triple(Screen.CreateHabit, Icons.Default.AddCircle, R.string.add_nav),
                        Triple(Screen.Profile, Icons.Default.Person, R.string.profile_nav)
                    )
                    navItems.forEach { (screen, icon, labelRes) ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp)) },
                            label = { Text(stringResource(labelRes)) },
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = com.example.habittrackerapp.ui.theme.EmeraldGreen,
                                indicatorColor = com.example.habittrackerapp.ui.theme.EmeraldGreen,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Row(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (showNavigation && isTablet) {
                NavigationRail(
                    containerColor = Color.White,
                    header = {
                        Icon(
                            imageVector = Icons.Default.AddCircle, // Replaced CheckCircle with AddCircle as placeholder or similar
                            contentDescription = null,
                            tint = com.example.habittrackerapp.ui.theme.EmeraldGreen,
                            modifier = Modifier.size(48.dp).padding(vertical = 16.dp)
                        )
                    }
                ) {
                    val navItems = listOf(
                        Triple(Screen.Home, Icons.Default.Home, R.string.home_nav),
                        Triple(Screen.CreateHabit, Icons.Default.AddCircle, R.string.add_nav),
                        Triple(Screen.Profile, Icons.Default.Person, R.string.profile_nav)
                    )
                    navItems.forEach { (screen, icon, labelRes) ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationRailItem(
                            icon = { Icon(icon, contentDescription = null) },
                            label = { Text(stringResource(labelRes)) },
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = com.example.habittrackerapp.ui.theme.EmeraldGreen,
                                indicatorColor = com.example.habittrackerapp.ui.theme.EmeraldGreen,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                SetupNavGraph(
                    navController = navController,
                    windowSizeClass = windowSizeClass,
                    startDestination = startDestination
                )
            }
        }
    }
}
