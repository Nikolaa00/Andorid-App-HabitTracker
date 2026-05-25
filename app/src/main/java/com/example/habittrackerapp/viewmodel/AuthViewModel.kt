package com.example.habittrackerapp.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittrackerapp.R
import com.example.habittrackerapp.data.repository.AuthRepository
import com.example.habittrackerapp.data.repository.HabitRepository
import com.example.habittrackerapp.domain.model.AppSettings
import com.example.habittrackerapp.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val repository: HabitRepository
) : ViewModel() {

    val currentUser = repository.userSession
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

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
        viewModelScope.launch {
            authRepository.logout()
            repository.clearSession()
            onSuccess()
        }
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
                            createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
                        _authState.value = AuthState.Error(message = "Firebase user is null")
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(message = exception.message ?: "Anonymous sign-in failed")
                }
        }
    }

    fun signInWithEmail(onSuccess: () -> Unit) {
        if (_emailField.value.isBlank() || _passwordField.value.isBlank()) {
            _authState.value = AuthState.Error(messageResId = R.string.error_empty_fields)
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(_emailField.value).matches()) {
            _authState.value = AuthState.Error(messageResId = R.string.error_invalid_email)
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            authRepository.loginWithEmail(_emailField.value, _passwordField.value)
                .onSuccess { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        val userId = firebaseUser.uid
                        
                        // Sync with local DB using cloud display name
                        val user = User(
                            uid = userId,
                            displayName = firebaseUser.displayName ?: "User",
                            email = _emailField.value,
                            photoUrl = firebaseUser.photoUrl?.toString(),
                            bio = null,
                            totalPoints = 0,
                            createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
                    } else {
                        _authState.value = AuthState.Error(message = "Firebase user is null")
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(message = exception.message ?: "Login failed")
                }
        }
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            authRepository.signInWithGoogle(idToken)
                .onSuccess { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        val userId = firebaseUser.uid
                        
                        // Sync with local DB
                        val user = User(
                            uid = userId,
                            displayName = firebaseUser.displayName ?: "User",
                            email = firebaseUser.email,
                            photoUrl = firebaseUser.photoUrl?.toString(),
                            bio = null,
                            totalPoints = 0,
                            createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
                    } else {
                        _authState.value = AuthState.Error(message = "Firebase user is null")
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(message = exception.message ?: "Google login failed")
                }
        }
    }

    fun signInWithFacebook(accessToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            authRepository.signInWithFacebook(accessToken)
                .onSuccess { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        val userId = firebaseUser.uid
                        
                        val user = User(
                            uid = userId,
                            displayName = firebaseUser.displayName ?: "Facebook User",
                            email = firebaseUser.email,
                            photoUrl = firebaseUser.photoUrl?.toString(),
                            bio = null,
                            totalPoints = 0,
                            createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        )
                        
                        repository.upsertUser(user)
                        
                        repository.upsertSettings(AppSettings(
                            userId = userId,
                            isDarkMode = false,
                            notificationsEnabled = true,
                            preferredLanguage = "en"
                        ))
                        
                        _authState.value = AuthState.Success
                        onSuccess()
                    } else {
                        _authState.value = AuthState.Error(message = "Firebase user is null")
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(message = exception.message ?: "Facebook login failed")
                }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String? = null, val messageResId: Int? = null) : AuthState()
}
