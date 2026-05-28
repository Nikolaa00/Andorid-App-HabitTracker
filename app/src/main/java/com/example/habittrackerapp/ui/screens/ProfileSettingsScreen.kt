package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habittrackerapp.R
import com.example.habittrackerapp.navigation.Screen
import com.example.habittrackerapp.ui.theme.EmeraldGreen
import com.example.habittrackerapp.ui.theme.LightGrayBorder
import com.example.habittrackerapp.viewmodel.AuthViewModel

@Composable
fun ProfileSettingsScreen(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    var notificationsEnabled by remember { mutableStateOf(true) }
    val isPhoneLandscape = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    val spacing = if (isPhoneLandscape) 8.dp else 16.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = if (isPhoneLandscape) 32.dp else 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(spacing))
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(if (isPhoneLandscape) 16.dp else 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val displayName = if (currentUser?.displayName != null && currentUser?.email != null) {
                    currentUser?.displayName ?: ""
                } else {
                    "User"
                }
                Text(
                    text = displayName,
                    fontSize = if (isPhoneLandscape) 22.sp else 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(spacing))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(if (isPhoneLandscape) 16.dp else 24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF1A73E8))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = stringResource(R.string.header_preferences), fontSize = if (isPhoneLandscape) 18.sp else 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(if (isPhoneLandscape) 4.dp else 8.dp))
                Text(text = stringResource(R.string.desc_preferences), fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(spacing))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.label_notifications), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = EmeraldGreen)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(spacing))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(if (isPhoneLandscape) 16.dp else 24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD32F2F))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = stringResource(R.string.header_danger_zone), fontSize = if (isPhoneLandscape) 18.sp else 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(if (isPhoneLandscape) 4.dp else 8.dp))
                Text(text = stringResource(R.string.desc_danger_zone), fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(if (isPhoneLandscape) 16.dp else 24.dp))
                
                OutlinedButton(
                    onClick = { 
                        viewModel.logout {
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(if (isPhoneLandscape) 48.dp else 56.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp)
                ) {
                    Text(text = stringResource(R.string.btn_logout), color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
