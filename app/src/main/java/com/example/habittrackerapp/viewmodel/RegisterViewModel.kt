package com.example.habittrackerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapp.data.repository.AuthRepository
import com.example.habittrackerapp.data.repository.HabitRepository
import com.example.habittrackerapp.domain.model.AppSettings
import com.example.habittrackerapp.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val repository: HabitRepository
) : ViewModel() {

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

    private suspend fun initializeUser(userId: String, displayName: String, email: String?) {
        val user = User(
            uid = userId,
            displayName = displayName,
            email = email,
            photoUrl = null,
            bio = null,
            totalPoints = 0,
            joinedDate = System.currentTimeMillis()
        )
        repository.upsertUser(user)
        repository.upsertSettings(AppSettings(
            userId = userId,
            isDarkMode = false,
            notificationsEnabled = true,
            preferredLanguage = "en"
        ))
    }

    fun register(onSuccess: () -> Unit) {
        if (_password.value != _confirmPassword.value) {
            _errorMessage.value = "Passwords do not match"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            // Simulation for now, Part 3 will replace this
            val userId = UUID.randomUUID().toString()
            initializeUser(userId, _username.value, _email.value)
            
            _isLoading.value = false
            onSuccess()
        }
    }

    fun registerWithGoogle(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            
            val userId = UUID.randomUUID().toString()
            initializeUser(userId, "Google User", null)
            
            _isLoading.value = false
            onSuccess()
        }
    }

    fun registerWithFacebook(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            
            val userId = UUID.randomUUID().toString()
            initializeUser(userId, "Facebook User", null)
            
            _isLoading.value = false
            onSuccess()
        }
    }

    fun continueAsGuest(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.signInAnonymously()
                .onSuccess { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        initializeUser(firebaseUser.uid, "Guest User", null)
                        _isLoading.value = false
                        onSuccess()
                    } else {
                        _errorMessage.value = "Firebase user is null"
                        _isLoading.value = false
                    }
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Anonymous sign-in failed"
                    _isLoading.value = false
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
