package com.example.habittrackerapp.ui.screens.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittrackerapp.R
import com.example.habittrackerapp.ui.theme.DarkNavy
import com.example.habittrackerapp.ui.theme.PrimaryGreen

@Composable
fun AddEditHabitScreen(
    habitId: String? = null,
    onSaveClick: (HabitData) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var goalValue by remember { mutableIntStateOf(8) }
    var selectedDays by remember { mutableStateOf(setOf("M", "T", "W", "T", "F")) }

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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            SectionCard(
                icon = Icons.AutoMirrored.Filled.PlaylistAdd,
                title = stringResource(R.string.general_info)
            ) {
                HabitTextField(
                    label = stringResource(R.string.habit_name),
                    value = name,
                    onValueChange = { name = it },
                    placeholder = stringResource(R.string.habit_name_hint)
                )
                Spacer(modifier = Modifier.height(20.dp))
                HabitTextField(
                    label = stringResource(R.string.habit_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.habit_description_hint)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionCard(
                icon = Icons.Default.CalendarToday,
                title = stringResource(R.string.frequency)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val days = listOf("M", "T", "W", "T", "F", "S", "S")
                    days.forEachIndexed { index, day ->
                        DaySelector(
                            day = day,
                            isSelected = selectedDays.contains(day),
                            onClick = {
                                selectedDays = if (selectedDays.contains(day)) {
                                    selectedDays - day
                                } else {
                                    selectedDays + day
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.frequency_msg),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionCard(
                icon = Icons.Default.FilterTiltShift,
                title = stringResource(R.string.daily_goal)
            ) {
                Surface(
                    color = Color(0xFFF1F5F9).copy(alpha = 0.5f),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { if (goalValue > 1) goalValue-- },
                            modifier = Modifier.background(Color.White, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = null)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = goalValue.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = DarkNavy
                            )
                            Text(
                                text = stringResource(R.string.times_per_day),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(
                            onClick = { goalValue++ },
                            modifier = Modifier.background(Color.White, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionCard(
                icon = Icons.Default.NotificationsNone,
                title = stringResource(R.string.reminders)
            ) {
                Text(
                    text = stringResource(R.string.reminders_msg),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFFE2E8F0).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "08:00 AM",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            color = Color(0xFF1D4ED8),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Transparent, CircleShape)
                            .padding(1.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Using a simple dashed border simulation or just an icon
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.LightGray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { onSaveClick(HabitData(name, description, goalValue, selectedDays.toList())) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkNavy),
                shape = RoundedCornerShape(32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (habitId == null) stringResource(R.string.create_habit) else stringResource(R.string.edit_habit),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SectionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = PrimaryGreen)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = DarkNavy,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            content()
        }
    }
}

@Composable
fun HabitTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = Color(0xFFE2E8F0)
            )
        )
    }
}

@Composable
fun DaySelector(day: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(if (isSelected) PrimaryGreen else Color.White, CircleShape)
            .then(if (!isSelected) Modifier.background(Color.White, CircleShape).padding(1.dp).background(Color.White, CircleShape) else Modifier)
            .then(if (!isSelected) Modifier.padding(1.dp) else Modifier) // Simulating border
            // Simplified:
            .background(if (isSelected) PrimaryGreen else Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            onClick = onClick,
            color = Color.Transparent,
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .then(if(!isSelected) Modifier.background(Color.Transparent, CircleShape).padding(1.dp).background(Color.White, CircleShape) else Modifier),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    color = if (isSelected) Color.White else Color.LightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

data class HabitData(
    val name: String,
    val description: String,
    val goalValue: Int,
    val frequency: List<String>
)
