package com.example.habittrackerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _emailField = MutableStateFlow("")
    val emailField: StateFlow<String> = _emailField.asStateFlow()

    private val _passwordField = MutableStateFlow("")
    val passwordField: StateFlow<String> = _passwordField.asStateFlow()

    private val _usernameField = MutableStateFlow("")
    val usernameField: StateFlow<String> = _usernameField.asStateFlow()

    private val _confirmPasswordField = MutableStateFlow("")
    val confirmPasswordField: StateFlow<String> = _confirmPasswordField.asStateFlow()

    fun onEmailChange(value: String) {
        _emailField.value = value
    }

    fun onPasswordChange(value: String) {
        _passwordField.value = value
    }

    fun onUsernameChange(value: String) {
        _usernameField.value = value
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPasswordField.value = value
    }

    fun signInAnonymously(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Placeholder logic
            _authState.value = AuthState.Success
            onSuccess()
        }
    }

    fun registerWithEmail(email: String, password: String, username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Placeholder logic
            _authState.value = AuthState.Success
            onSuccess()
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
