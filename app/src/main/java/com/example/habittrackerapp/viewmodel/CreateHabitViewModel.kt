package com.example.habittrackerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapp.data.repository.HabitRepository
import com.example.habittrackerapp.domain.model.Habit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateHabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    // Default daily goal set to 1, user UI removed
    private val _dailyGoal = MutableStateFlow(1)
    val dailyGoal: StateFlow<Int> = _dailyGoal.asStateFlow()

    private val _frequency = MutableStateFlow<List<Int>>(listOf(1, 2, 3, 4, 5, 6, 7)) // Mon-Sun
    val frequency: StateFlow<List<Int>> = _frequency.asStateFlow()

    private val _reminders = MutableStateFlow<List<LocalTime>>(emptyList())
    val reminders: StateFlow<List<LocalTime>> = _reminders.asStateFlow()

    fun onNameChange(value: String) { _name.value = value }
    fun onDescriptionChange(value: String) { _description.value = value }
    
    fun toggleFrequencyDay(day: Int) {
        if (_frequency.value.contains(day)) {
            _frequency.value = _frequency.value - day
        } else {
            _frequency.value = (_frequency.value + day).sorted()
        }
    }

    fun addReminder(time: LocalTime) {
        if (!_reminders.value.contains(time)) {
            _reminders.value = (_reminders.value + time).sorted()
        }
    }

    fun removeReminder(time: LocalTime) {
        _reminders.value -= time
    }

    fun saveHabit(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentUserId = repository.userSession.value?.uid ?: return@launch
            if (_name.value.isBlank()) return@launch
            
            val newHabit = Habit(
                userId = currentUserId,
                name = _name.value,
                description = _description.value,
                frequency = _frequency.value,
                dailyGoal = _dailyGoal.value, // Still uses the default value 1
                currentProgress = 0,
                reminders = _reminders.value,
                lastUpdated = System.currentTimeMillis(),
                createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
            repository.insertHabit(newHabit)
            onSuccess()
        }
    }
}
