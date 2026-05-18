package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.habittrackerapp.R
import com.example.habittrackerapp.navigation.Screen
import com.example.habittrackerapp.ui.theme.DeepSlateBlue
import com.example.habittrackerapp.ui.theme.EmeraldGreen

@Composable
fun WelcomeScreen(
    navController: NavController,
    windowSizeClass: WindowSizeClass
) {
    val isLargeScreen = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    val isPhoneLandscape = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    val buttonModifier = if (isLargeScreen) {
        Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth(0.5f)
    } else {
        Modifier.fillMaxWidth()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .then(if (isLargeScreen) Modifier.width(600.dp) else Modifier.fillMaxSize())
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isPhoneLandscape) Arrangement.Top else Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(if (isPhoneLandscape) 64.dp else 120.dp)
                    .background(EmeraldGreen, RoundedCornerShape(if (isPhoneLandscape) 16.dp else 24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(if (isPhoneLandscape) 32.dp else 60.dp)
                )
            }

            Spacer(modifier = Modifier.height(if (isPhoneLandscape) 16.dp else 32.dp))

            Text(
                text = stringResource(R.string.welcome_to_habit_tracker),
                fontSize = if (isPhoneLandscape) 20.sp else 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = if (isPhoneLandscape) 28.sp else 36.sp
            )

            if (!isPhoneLandscape) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.welcome_description),
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(if (isPhoneLandscape) 24.dp else 64.dp))

            Button(
                onClick = { navController.navigate(Screen.Login.route) },
                modifier = buttonModifier.height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.login),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(if (isPhoneLandscape) 8.dp else 16.dp))

            Button(
                onClick = { navController.navigate(Screen.Register.route) },
                modifier = buttonModifier.height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepSlateBlue),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.register),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(if (isPhoneLandscape) 8.dp else 16.dp))

            OutlinedButton(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = buttonModifier.height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp)
            ) {
                Text(
                    text = stringResource(R.string.continue_as_guest),
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(if (isPhoneLandscape) 24.dp else 48.dp))

            Text(
                text = stringResource(R.string.habits_shape_future),
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}
