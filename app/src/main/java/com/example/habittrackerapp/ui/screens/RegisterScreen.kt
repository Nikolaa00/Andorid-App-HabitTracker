package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.habittrackerapp.R
import com.example.habittrackerapp.navigation.Screen
import com.example.habittrackerapp.ui.theme.EmeraldGreen
import com.example.habittrackerapp.ui.theme.LightGrayBorder
import com.example.habittrackerapp.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: RegisterViewModel
) {
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val isLargeScreen = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    val isPhoneLandscape = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    
    val horizontalPadding = if (isLargeScreen) 80.dp else 24.dp
    val verticalPadding = if (isPhoneLandscape) 8.dp else 32.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .then(if (isLargeScreen) Modifier.width(600.dp).fillMaxHeight() else Modifier.fillMaxWidth())
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isLargeScreen) 
                Arrangement.spacedBy(16.dp, Alignment.CenterVertically) 
            else 
                Arrangement.spacedBy(if (isPhoneLandscape) 8.dp else 16.dp)
        ) {
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Scaled down logo for phone landscape
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
                            text = stringResource(R.string.create_account),
                            fontSize = if (isPhoneLandscape) 20.sp else 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )

                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 4.dp else 8.dp))
                        
                        Text(
                            text = stringResource(R.string.start_building_better_habits),
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
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(if (isPhoneLandscape) 12.dp else 24.dp)) {
                        RegisterInputField(
                            label = stringResource(R.string.username_label),
                            value = username,
                            onValueChange = viewModel::onUsernameChange,
                            placeholder = stringResource(R.string.username_hint),
                        )
                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 8.dp else 16.dp))
                        RegisterInputField(
                            label = stringResource(R.string.email_address_full),
                            value = email,
                            onValueChange = viewModel::onEmailChange,
                            placeholder = stringResource(R.string.email_address_hint),
                        )
                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 8.dp else 16.dp))
                        RegisterInputField(
                            label = stringResource(R.string.password_label),
                            value = password,
                            onValueChange = viewModel::onPasswordChange,
                            placeholder = "••••••••",
                            isPassword = true
                        )
                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 8.dp else 16.dp))
                        RegisterInputField(
                            label = stringResource(R.string.confirm_password_label),
                            value = confirmPassword,
                            onValueChange = viewModel::onConfirmPasswordChange,
                            placeholder = "••••••••",
                            isPassword = true
                        )

                        errorMessage?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 12.dp else 24.dp))

                        Button(
                            onClick = { viewModel.register { navController.navigate(Screen.Home.route) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(28.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                            } else {
                                Text(
                                    text = stringResource(R.string.register),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 12.dp else 24.dp))

                        // OR Divider
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = LightGrayBorder)
                            Text(
                                text = " " + stringResource(R.string.or_divider) + " ",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = LightGrayBorder)
                        }

                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 12.dp else 24.dp))

                        // Social Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SocialAuthButton(
                                text = stringResource(R.string.google),
                                icon = painterResource(id = R.drawable.ic_google),
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.registerWithGoogle { navController.navigate(Screen.Home.route) } }
                            )
                            SocialAuthButton(
                                text = stringResource(R.string.facebook),
                                icon = painterResource(id = R.drawable.ic_facebook),
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.registerWithFacebook { navController.navigate(Screen.Home.route) } }
                            )
                        }

                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 8.dp else 16.dp))

                        // Continue as Guest
                        OutlinedButton(
                            onClick = { viewModel.continueAsGuest { navController.navigate(Screen.Home.route) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(28.dp),
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.continue_as_guest),
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Footer Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(if (isPhoneLandscape) 4.dp else 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.already_have_account),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                            Text(
                                text = stringResource(R.string.login),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2)
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun RegisterInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldGreen,
                unfocusedBorderColor = LightGrayBorder,
                focusedContainerColor = Color(0xFFFBFBFB),
                unfocusedContainerColor = Color(0xFFFBFBFB)
            )
        )
    }
}

@Composable
fun SocialAuthButton(
    text: String,
    icon: Painter,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
        border = if (containerColor == Color.White) androidx.compose.foundation.BorderStroke(1.dp, LightGrayBorder) else null
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
