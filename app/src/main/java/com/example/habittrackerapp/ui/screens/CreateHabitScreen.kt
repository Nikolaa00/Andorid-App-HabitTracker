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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habittrackerapp.R
import com.example.habittrackerapp.ui.screens.components.ReminderDialog
import com.example.habittrackerapp.ui.theme.EmeraldGreen
import com.example.habittrackerapp.ui.theme.LightGrayBorder
import com.example.habittrackerapp.viewmodel.CreateHabitViewModel
import java.time.format.DateTimeFormatter

@Composable
fun CreateHabitScreen(
    navController: NavController,
    viewModel: CreateHabitViewModel = hiltViewModel()
) {
    var habitName by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }
    var selectedDays by remember { mutableStateOf(setOf(0, 1, 2, 3, 4)) }
    var dailyGoal by remember { mutableIntStateOf(8) }

    val reminders by viewModel.reminders.collectAsState()
    var showReminderDialog by remember { mutableStateOf(false) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a") }

    if (showReminderDialog) {
        ReminderDialog(
            onDismiss = { showReminderDialog = false },
            onConfirm = { time ->
                viewModel.addReminder(time)
                showReminderDialog = false
            }
        )
    }

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
                    Text(text = stringResource(R.string.header_general), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(text = stringResource(R.string.label_habit_name), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = habitName,
                    onValueChange = { habitName = it },
                    placeholder = { Text(stringResource(R.string.hint_habit_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreen, unfocusedBorderColor = LightGrayBorder)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = stringResource(R.string.label_habit_desc), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = habitDescription,
                    onValueChange = { habitDescription = it },
                    placeholder = { Text(stringResource(R.string.hint_habit_desc)) },
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
                    Text(text = stringResource(R.string.header_frequency), fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                Text(text = stringResource(R.string.frequency_subtext), fontSize = 14.sp, color = Color.Gray)
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
                    Text(text = stringResource(R.string.header_goal), fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                        Text(text = stringResource(R.string.counter_times_per_day), fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
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
                    Text(text = stringResource(R.string.header_reminders), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.reminders_description), fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                
                // Reminders List
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    reminders.forEach { time ->
                        Surface(
                            color = Color(0xFFE3F2FD),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.clickable { viewModel.removeReminder(time) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = time.format(timeFormatter),
                                    color = Color(0xFF1A73E8),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color(0xFF1A73E8),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    // Add Button
                    IconButton(
                        onClick = { showReminderDialog = true },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE3F2FD), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color(0xFF1A73E8),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                if (reminders.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_reminders),
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
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
            Text(text = stringResource(R.string.btn_create_habit), fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
