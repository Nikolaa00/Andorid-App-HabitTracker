package com.example.habittrackerapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habittrackerapp.R
import com.example.habittrackerapp.navigation.Screen
import com.example.habittrackerapp.ui.theme.EmeraldGreen
import com.example.habittrackerapp.ui.theme.LightGrayBorder
import com.example.habittrackerapp.viewmodel.AuthState
import com.example.habittrackerapp.viewmodel.AuthViewModel

@Composable
fun SignInScreen(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val email by viewModel.emailField.collectAsState()
    val password by viewModel.passwordField.collectAsState()
    val authState by viewModel.authState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isLargeScreen = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    val isPhoneLandscape = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    
    val horizontalPadding = if (isLargeScreen) 80.dp else 24.dp
    val verticalPadding = if (isPhoneLandscape) 8.dp else 32.dp

    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .then(
                    if (isLargeScreen) 
                        Modifier.width(600.dp).fillMaxHeight() 
                    else 
                        Modifier.fillMaxWidth()
                )
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isLargeScreen) 
                Arrangement.spacedBy(16.dp, Alignment.CenterVertically) 
            else 
                Arrangement.spacedBy(if (isPhoneLandscape) 8.dp else 16.dp)
        ) {
            // Header Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(if (isPhoneLandscape) 12.dp else 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isPhoneLandscape) 40.dp else 64.dp)
                                .background(Color(0xFFE8F5E9), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(if (isPhoneLandscape) 20.dp else 32.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 8.dp else 24.dp))

                        Text(
                            text = stringResource(R.string.welcome_back),
                            fontSize = if (isPhoneLandscape) 20.sp else 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 4.dp else 8.dp))
                        
                        Text(
                            text = stringResource(R.string.sign_in_to_continue),
                            fontSize = if (isPhoneLandscape) 12.sp else 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Input Fields Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(if (isPhoneLandscape) 12.dp else 24.dp)) {
                        Text(
                            text = stringResource(R.string.email_address_label),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = viewModel::onEmailChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.email_hint)) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldGreen,
                                unfocusedBorderColor = LightGrayBorder
                            ),
                            enabled = authState !is AuthState.Loading
                        )

                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 8.dp else 16.dp))

                        Text(
                            text = stringResource(R.string.password_label),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = viewModel::onPasswordChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("••••••••") },
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldGreen,
                                unfocusedBorderColor = LightGrayBorder
                            ),
                            enabled = authState !is AuthState.Loading
                        )

                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 12.dp else 24.dp))

                        Button(
                            onClick = { 
                                viewModel.signInWithEmail {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Welcome.route) { inclusive = true }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(28.dp),
                            enabled = authState !is AuthState.Loading
                        ) {
                            Text(
                                text = stringResource(R.string.sign_in_arrow),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Footer Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(if (isPhoneLandscape) 4.dp else 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dont_have_account_register),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        TextButton(
                            onClick = { navController.navigate(Screen.Register.route) },
                            enabled = authState !is AuthState.Loading
                        ) {
                            Text(
                                text = stringResource(R.string.register),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2)
                            )
                        }
                    }
                }
            }
        }

        if (authState is AuthState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = EmeraldGreen)
            }
        }
    }
}
