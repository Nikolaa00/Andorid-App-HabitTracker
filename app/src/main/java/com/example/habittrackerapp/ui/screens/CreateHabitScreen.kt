package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.habittrackerapp.R
import com.example.habittrackerapp.ui.theme.EmeraldGreen
import com.example.habittrackerapp.ui.theme.LightGrayBorder

@Composable
fun CreateHabitScreen(navController: NavController) {
    var habitName by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }
    var selectedDays by remember { mutableStateOf(setOf(0, 1, 2, 3, 4)) }
    var dailyGoal by remember { mutableStateOf(8) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EditNote, contentDescription = null, tint = EmeraldGreen)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = stringResource(R.string.general_info), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(text = stringResource(R.string.habit_name_label), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = habitName,
                    onValueChange = { habitName = it },
                    placeholder = { Text(stringResource(R.string.habit_name_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreen, unfocusedBorderColor = LightGrayBorder)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = stringResource(R.string.habit_description_label), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = habitDescription,
                    onValueChange = { habitDescription = it },
                    placeholder = { Text(stringResource(R.string.habit_description_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreen, unfocusedBorderColor = LightGrayBorder)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = EmeraldGreen)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = stringResource(R.string.frequency), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    val days = listOf(
                        stringResource(R.string.day_m), stringResource(R.string.day_t), stringResource(R.string.day_w),
                        stringResource(R.string.day_th), stringResource(R.string.day_f), stringResource(R.string.day_s),
                        stringResource(R.string.day_su)
                    )
                    days.forEachIndexed { index, day ->
                        DayChip(
                            label = day,
                            isSelected = index in selectedDays,
                            onClick = {
                                selectedDays = if (index in selectedDays) selectedDays - index else selectedDays + index
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.repeat_weekly), fontSize = 14.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TrackChanges, contentDescription = null, tint = EmeraldGreen)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = stringResource(R.string.daily_goal_label), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFF1F5F9)).padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { if (dailyGoal > 1) dailyGoal-- }, modifier = Modifier.background(Color.White, CircleShape)) {
                        Icon(Icons.Default.Remove, contentDescription = null)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = dailyGoal.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(text = stringResource(R.string.times_per_day), fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = { dailyGoal++ }, modifier = Modifier.background(Color.White, CircleShape)) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = EmeraldGreen)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = stringResource(R.string.reminders), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.reminders_description), fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFE3F2FD), shape = RoundedCornerShape(12.dp)) {
                        Text(text = stringResource(R.string.reminder_time_default), modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = Color(0xFF1A73E8), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = {}, modifier = Modifier.border(1.dp, LightGrayBorder, CircleShape)) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(32.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.create_habit_button), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DayChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isSelected) EmeraldGreen else Color.White)
            .border(1.dp, if (isSelected) EmeraldGreen else LightGrayBorder, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = if (isSelected) Color.White else Color.Black, fontWeight = FontWeight.Bold)
    }
}
