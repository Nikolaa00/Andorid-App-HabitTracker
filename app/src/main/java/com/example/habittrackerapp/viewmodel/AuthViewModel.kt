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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val repository: HabitRepository
) : ViewModel() {

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

    fun clearError() {
        _authState.value = AuthState.Idle
    }

    fun logout(onSuccess: () -> Unit) {
        authRepository.logout()
        onSuccess()
    }

    fun signInAnonymously(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            authRepository.signInAnonymously()
                .onSuccess { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        val userId = firebaseUser.uid
                        val anonymousUser = User(
                            uid = userId,
                            displayName = "Anonymous User",
                            email = null,
                            photoUrl = null,
                            bio = null,
                            totalPoints = 0,
                            joinedDate = System.currentTimeMillis()
                        )
                        
                        repository.upsertUser(anonymousUser)
                        
                        // Initialize default settings
                        repository.upsertSettings(AppSettings(
                            userId = userId,
                            isDarkMode = false,
                            notificationsEnabled = true,
                            preferredLanguage = "en"
                        ))
                        
                        _authState.value = AuthState.Success
                        onSuccess()
                    } else {
                        _authState.value = AuthState.Error("Firebase user is null")
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Anonymous sign-in failed")
                }
        }
    }

    fun signInWithEmail(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            // Simulation of Firebase Auth successful login
            // Will be replaced in Part 3
            val userId = UUID.randomUUID().toString()
            val user = User(
                uid = userId,
                displayName = "Logged User",
                email = _emailField.value,
                photoUrl = null,
                bio = null,
                totalPoints = 0,
                joinedDate = System.currentTimeMillis()
            )
            
            repository.upsertUser(user)
            
            // Initialize default settings if not exists
            repository.upsertSettings(AppSettings(
                userId = userId,
                isDarkMode = false,
                notificationsEnabled = true,
                preferredLanguage = "en"
            ))
            
            _authState.value = AuthState.Success
            onSuccess()
        }
    }

    fun registerWithEmail(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            // Simulation of Firebase Auth successful registration
            // Will be replaced in Part 3
            val userId = UUID.randomUUID().toString()
            val newUser = User(
                uid = userId,
                displayName = _usernameField.value,
                email = _emailField.value,
                photoUrl = null,
                bio = null,
                totalPoints = 0,
                joinedDate = System.currentTimeMillis()
            )
            
            repository.upsertUser(newUser)
            
            // Initialize default settings
            repository.upsertSettings(AppSettings(
                userId = userId,
                isDarkMode = false,
                notificationsEnabled = true,
                preferredLanguage = "en"
            ))

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
