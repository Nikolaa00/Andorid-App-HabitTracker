package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habittrackerapp.R
import com.example.habittrackerapp.domain.model.Habit
import com.example.habittrackerapp.ui.theme.EmeraldGreen
import com.example.habittrackerapp.ui.theme.LightGrayBorder
import com.example.habittrackerapp.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    
    val isPhoneLandscape = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    val horizontalPadding = if (isPhoneLandscape) 32.dp else 16.dp
    val spacing = if (isPhoneLandscape) 8.dp else 16.dp

    val doneCount = habits.count { it.currentProgress >= it.dailyGoal }
    val totalCount = habits.size
    
    val totalGoals = habits.sumOf { it.dailyGoal }
    val totalProgress = habits.sumOf { it.currentProgress }
    val progressFraction = if (totalGoals > 0) totalProgress.toFloat() / totalGoals.toFloat() else 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = horizontalPadding),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        // Greeting Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(if (isPhoneLandscape) 16.dp else 24.dp)) {
                    val greeting = if (currentUser?.displayName != null && currentUser?.email != null) {
                        "Hello, ${currentUser?.displayName}!"
                    } else {
                        stringResource(R.string.greeting_user)
                    }
                    Text(
                        text = greeting,
                        fontSize = if (isPhoneLandscape) 20.sp else 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = stringResource(R.string.streak_info),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Daily Progress Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(if (isPhoneLandscape) 16.dp else 24.dp)) {
                    Text(
                        text = stringResource(R.string.daily_progress),
                        fontSize = if (isPhoneLandscape) 18.sp else 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.progress_subtext, doneCount, totalCount),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(spacing))
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isPhoneLandscape) 8.dp else 12.dp)
                            .background(Color(0xFFE2E8F0), CircleShape),
                        color = EmeraldGreen,
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.todays_habits),
                    fontSize = if (isPhoneLandscape) 18.sp else 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "$doneCount/$totalCount ${stringResource(R.string.done_badge)}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // Real Habit Items from DB
        items(habits, key = { it.id }) { habit ->
            HabitItem(
                habit = habit,
                onToggleComplete = {
                    val newProgress = if (habit.currentProgress >= habit.dailyGoal) 0 else habit.dailyGoal
                    viewModel.updateHabitProgress(habit, newProgress)
                }
            )
        }
    }
}

@Composable
fun HabitItem(habit: Habit, onToggleComplete: () -> Unit) {
    val isCompleted = habit.currentProgress >= habit.dailyGoal
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, LightGrayBorder, RoundedCornerShape(16.dp))
            .clickable { onToggleComplete() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = habit.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = if (habit.description.isNotBlank()) habit.description else "${habit.currentProgress} / ${habit.dailyGoal}",
                    fontSize = 14.sp, 
                    color = Color.Gray
                )
            }
            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(EmeraldGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            } else {
                Icon(
                    imageVector = Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
