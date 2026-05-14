package com.example.habittrackerapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.habittrackerapp.R
import com.example.habittrackerapp.navigation.Screen
import com.example.habittrackerapp.navigation.SetupNavGraph
import com.example.habittrackerapp.ui.components.LanguageSwitcherBar
import com.example.habittrackerapp.ui.theme.EmeraldGreen
import com.example.habittrackerapp.util.LocaleHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationShell(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val currentLanguage = remember { mutableStateOf(LocaleHelper.getSelectedLanguage()) }

    val bottomBarScreens = listOf(Screen.Home.route, Screen.Profile.route, Screen.CreateHabit.route)
    val showNavigation = currentDestination?.route in bottomBarScreens

    val isTablet = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    Scaffold(
        topBar = {
            if (showNavigation) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    },
                    actions = {
                        LanguageSwitcherBar(
                            currentLanguage = currentLanguage.value,
                            onLanguageChange = { lang ->
                                LocaleHelper.setLocale(lang)
                                currentLanguage.value = lang
                            }
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
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
                                selectedTextColor = EmeraldGreen,
                                indicatorColor = EmeraldGreen,
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
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldGreen,
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
                                selectedTextColor = EmeraldGreen,
                                indicatorColor = EmeraldGreen,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
            
            Box(modifier = Modifier.fillMaxSize()) {
                SetupNavGraph(navController = navController)
            }
        }
    }
}
