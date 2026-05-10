package com.example.habittrackerapp.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittrackerapp.ui.theme.DarkNavy
import com.example.habittrackerapp.ui.theme.PrimaryGreen

@Composable
fun StatisticsScreen() {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "HabitTrack",
                    style = MaterialTheme.typography.titleLarge,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .background(Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(PrimaryGreen, CircleShape)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("MK", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "EN",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Statistics coming soon!",
                style = MaterialTheme.typography.headlineMedium,
                color = DarkNavy,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
