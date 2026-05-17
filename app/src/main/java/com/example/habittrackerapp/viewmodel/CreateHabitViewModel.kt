package com.example.habittrackerapp.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateHabitViewModel @Inject constructor() : ViewModel() {

    private val _reminders = MutableStateFlow<List<LocalTime>>(emptyList())
    val reminders: StateFlow<List<LocalTime>> = _reminders.asStateFlow()

    fun addReminder(time: LocalTime) {
        if (!_reminders.value.contains(time)) {
            _reminders.value = (_reminders.value + time).sorted()
        }
    }

    fun removeReminder(time: LocalTime) {
        _reminders.value -= time
    }
}
