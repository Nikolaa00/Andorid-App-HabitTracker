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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    private val _errorResId = MutableStateFlow<Int?>(null)
    val errorResId: StateFlow<Int?> = _errorResId.asStateFlow()

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
            createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
        _errorResId.value = null
        _errorMessage.value = null

        if (_username.value.isBlank() || _email.value.isBlank() || _password.value.isBlank()) {
            _errorResId.value = R.string.error_empty_fields
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
            _errorResId.value = R.string.error_invalid_email
            return
        }

        if (_password.value.length < 6) {
            _errorResId.value = R.string.error_password_too_short
            return
        }

        if (_password.value != _confirmPassword.value) {
            _errorResId.value = R.string.error_passwords_dont_match
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            
            authRepository.registerWithEmail(_email.value, _password.value)
                .onSuccess { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        // Update cloud profile with username
                        authRepository.updateDisplayName(_username.value)

                        initializeUser(firebaseUser.uid, _username.value, _email.value)
                        _isLoading.value = false
                        onSuccess()
                    } else {
                        _errorMessage.value = "Firebase user is null"
                        _isLoading.value = false
                    }
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message
                    _isLoading.value = false
                }
        }
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.signInWithGoogle(idToken)
                .onSuccess { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        initializeUser(
                            firebaseUser.uid, 
                            firebaseUser.displayName ?: "Google User", 
                            firebaseUser.email
                        )
                        _isLoading.value = false
                        onSuccess()
                    } else {
                        _errorMessage.value = "Firebase user is null"
                        _isLoading.value = false
                    }
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Google login failed"
                    _isLoading.value = false
                }
        }
    }

    fun registerWithGoogle(onSuccess: () -> Unit) {
        // Obsolete if we use the above
    }

    fun registerWithFacebook(onSuccess: () -> Unit) {
        // Obsolete
    }

    fun signInWithFacebook(accessToken: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.signInWithFacebook(accessToken)
                .onSuccess { authResult ->
                    val firebaseUser = authResult.user
                    if (firebaseUser != null) {
                        initializeUser(
                            firebaseUser.uid, 
                            firebaseUser.displayName ?: "Facebook User", 
                            firebaseUser.email
                        )
                        _isLoading.value = false
                        onSuccess()
                    } else {
                        _errorMessage.value = "Firebase user is null"
                        _isLoading.value = false
                    }
                }
                .onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Facebook login failed"
                    _isLoading.value = false
                }
        }
    }

    fun continueAsGuest(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorResId.value = null
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
        _errorResId.value = null
        _errorMessage.value = null
    }
}
