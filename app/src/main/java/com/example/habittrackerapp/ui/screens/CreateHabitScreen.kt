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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
    windowSizeClass: WindowSizeClass,
    viewModel: CreateHabitViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val description by viewModel.description.collectAsState()
    val frequency by viewModel.frequency.collectAsState()
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

    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        if (isExpanded) {
            TwoPaneLayout(
                habitName = name,
                onHabitNameChange = viewModel::onNameChange,
                habitDescription = description,
                onHabitDescriptionChange = viewModel::onDescriptionChange,
                selectedDays = frequency,
                onSelectedDaysChange = { viewModel.toggleFrequencyDay(it) },
                reminders = reminders,
                timeFormatter = timeFormatter,
                onAddReminderClick = { showReminderDialog = true },
                onRemoveReminder = viewModel::removeReminder,
                onCreateClick = { 
                    viewModel.saveHabit {
                        navController.popBackStack()
                    }
                }
            )
        } else {
            SinglePaneLayout(
                habitName = name,
                onHabitNameChange = viewModel::onNameChange,
                habitDescription = description,
                onHabitDescriptionChange = viewModel::onDescriptionChange,
                selectedDays = frequency,
                onSelectedDaysChange = { viewModel.toggleFrequencyDay(it) },
                reminders = reminders,
                timeFormatter = timeFormatter,
                onAddReminderClick = { showReminderDialog = true },
                onRemoveReminder = viewModel::removeReminder,
                onCreateClick = { 
                    viewModel.saveHabit {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}

@Composable
private fun SinglePaneLayout(
    habitName: String,
    onHabitNameChange: (String) -> Unit,
    habitDescription: String,
    onHabitDescriptionChange: (String) -> Unit,
    selectedDays: List<Int>,
    onSelectedDaysChange: (Int) -> Unit,
    reminders: List<java.time.LocalTime>,
    timeFormatter: DateTimeFormatter,
    onAddReminderClick: () -> Unit,
    onRemoveReminder: (java.time.LocalTime) -> Unit,
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GeneralInfoCard(habitName, onHabitNameChange, habitDescription, onHabitDescriptionChange)
        FrequencyCard(selectedDays, onSelectedDaysChange)
        RemindersCard(reminders, timeFormatter, onAddReminderClick, onRemoveReminder)
        
        Spacer(modifier = Modifier.height(8.dp))
        CreateButton(onCreateClick)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TwoPaneLayout(
    habitName: String,
    onHabitNameChange: (String) -> Unit,
    habitDescription: String,
    onHabitDescriptionChange: (String) -> Unit,
    selectedDays: List<Int>,
    onSelectedDaysChange: (Int) -> Unit,
    reminders: List<java.time.LocalTime>,
    timeFormatter: DateTimeFormatter,
    onAddReminderClick: () -> Unit,
    onRemoveReminder: (java.time.LocalTime) -> Unit,
    onCreateClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Left Column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GeneralInfoCard(habitName, onHabitNameChange, habitDescription, onHabitDescriptionChange)
            FrequencyCard(selectedDays, onSelectedDaysChange)
        }

        // Right Column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RemindersCard(reminders, timeFormatter, onAddReminderClick, onRemoveReminder)
            Spacer(modifier = Modifier.height(8.dp))
            CreateButton(onCreateClick)
        }
    }
}

@Composable
private fun GeneralInfoCard(
    habitName: String,
    onHabitNameChange: (String) -> Unit,
    habitDescription: String,
    onHabitDescriptionChange: (String) -> Unit
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
            
            Text(text = stringResource(R.string.label_habit_name).uppercase(), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.ExtraBold)
            OutlinedTextField(
                value = habitName,
                onValueChange = onHabitNameChange,
                placeholder = { Text(stringResource(R.string.hint_habit_name)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreen, unfocusedBorderColor = LightGrayBorder)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(R.string.label_habit_desc).uppercase(), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.ExtraBold)
            OutlinedTextField(
                value = habitDescription,
                onValueChange = onHabitDescriptionChange,
                placeholder = { Text(stringResource(R.string.hint_habit_desc)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreen, unfocusedBorderColor = LightGrayBorder)
            )
        }
    }
}

@Composable
private fun FrequencyCard(
    selectedDays: List<Int>,
    onSelectedDayToggle: (Int) -> Unit
) {
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
                        isSelected = (index + 1) in selectedDays,
                        onClick = { onSelectedDayToggle(index + 1) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.frequency_subtext), fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RemindersCard(
    reminders: List<java.time.LocalTime>,
    timeFormatter: DateTimeFormatter,
    onAddReminderClick: () -> Unit,
    onRemoveReminder: (java.time.LocalTime) -> Unit
) {
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
            
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                reminders.forEach { time ->
                    Surface(
                        color = Color(0xFFE3F2FD),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { onRemoveReminder(time) }
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

                IconButton(
                    onClick = onAddReminderClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE3F2FD), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Color(0xFF1A73E8),
                        modifier = Modifier.size(24.dp)
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
}

@Composable
private fun CreateButton(onCreateClick: () -> Unit) {
    Button(
        onClick = onCreateClick,
        modifier = Modifier.fillMaxWidth().height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
        shape = RoundedCornerShape(32.dp)
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.btn_create_habit), fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
