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
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onUsernameChange(value: String) {
        _username.value = value
    }

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
    }

    fun register(onSuccess: () -> Unit) {
        if (_password.value != _confirmPassword.value) {
            _errorMessage.value = "Passwords do not match" // This should ideally be a string resource ID in a real app, but for now...
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            // Simulate API call
            kotlinx.coroutines.delay(1500)
            _isLoading.value = false
            onSuccess()
        }
    }

    fun registerWithGoogle(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            // Simulate Google Login
            kotlinx.coroutines.delay(1000)
            _isLoading.value = false
            onSuccess()
        }
    }

    fun registerWithFacebook(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            // Simulate Facebook Login
            kotlinx.coroutines.delay(1000)
            _isLoading.value = false
            onSuccess()
        }
    }

    fun continueAsGuest(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            // Simulate Guest Login
            kotlinx.coroutines.delay(500)
            _isLoading.value = false
            onSuccess()
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
