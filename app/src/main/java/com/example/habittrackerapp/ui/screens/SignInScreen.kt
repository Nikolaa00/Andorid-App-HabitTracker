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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habittrackerapp.R
import com.example.habittrackerapp.navigation.Screen
import com.example.habittrackerapp.ui.components.SocialAuthButton
import com.example.habittrackerapp.ui.theme.EmeraldGreen
import com.example.habittrackerapp.ui.theme.LightGrayBorder
import com.example.habittrackerapp.viewmodel.AuthState
import com.example.habittrackerapp.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    
    // Define localized strings at the top to avoid LocalContext.current lint warnings
    val fbCancelledMsg = stringResource(R.string.error_facebook_signin_cancelled)
    val fbFailedMsg = stringResource(R.string.error_facebook_signin_failed)
    val googleFailedMsg = stringResource(R.string.error_google_signin_failed)
    val webClientId = stringResource(R.string.default_web_client_id)
    val authFailedMsg = "Authentication failed" 

    val emptyFieldsMsg = stringResource(R.string.error_empty_fields)
    val invalidEmailMsg = stringResource(R.string.error_invalid_email)

    val callbackManager = remember { CallbackManager.Factory.create() }
    
    // Facebook login launcher using legacy result API (needed for CallbackManager sync)
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            callbackManager.onActivityResult(64206, result.resultCode, result.data)
        }
    )

    DisposableEffect(Unit) {
        val callback = object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                viewModel.signInWithFacebook(result.accessToken.token) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            }

            override fun onCancel() {
                viewModel.clearError()
                Toast.makeText(context, fbCancelledMsg, Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(context, "$fbFailedMsg: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
        LoginManager.getInstance().registerCallback(callbackManager, callback)
        onDispose {
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }

    val isLargeScreen = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    val isPhoneLandscape = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    
    val horizontalPadding = if (isLargeScreen) 80.dp else 24.dp
    val verticalPadding = if (isPhoneLandscape) 8.dp else 32.dp

    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            val error = authState as AuthState.Error
            val message = when {
                error.messageResId == R.string.error_empty_fields -> emptyFieldsMsg
                error.messageResId == R.string.error_invalid_email -> invalidEmailMsg
                error.message != null -> error.message
                else -> authFailedMsg
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
                            text = stringResource(R.string.email_address_label).uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = viewModel::onEmailChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.email_hint), color = Color.LightGray) },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldGreen,
                                unfocusedBorderColor = LightGrayBorder,
                                focusedContainerColor = Color(0xFFFBFBFB),
                                unfocusedContainerColor = Color(0xFFFBFBFB)
                            ),
                            enabled = authState !is AuthState.Loading
                        )

                        Spacer(modifier = Modifier.height(if (isPhoneLandscape) 8.dp else 16.dp))

                        Text(
                            text = stringResource(R.string.password_label).uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = viewModel::onPasswordChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("••••••••", color = Color.LightGray) },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
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
                                unfocusedBorderColor = LightGrayBorder,
                                focusedContainerColor = Color(0xFFFBFBFB),
                                unfocusedContainerColor = Color(0xFFFBFBFB)
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
                            if (authState is AuthState.Loading) {
                                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                            } else {
                                Text(
                                    text = stringResource(R.string.sign_in_arrow),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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

                        Spacer(modifier = Modifier.height(16.dp))

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
                                onClick = {
                                    coroutineScope.launch {
                                        val googleIdOption = GetGoogleIdOption.Builder()
                                            .setFilterByAuthorizedAccounts(false)
                                            .setServerClientId(webClientId)
                                            .setAutoSelectEnabled(true)
                                            .build()

                                        val request = GetCredentialRequest.Builder()
                                            .addCredentialOption(googleIdOption)
                                            .build()

                                        try {
                                            val result = credentialManager.getCredential(
                                                context = context,
                                                request = request
                                            )
                                            val credential = result.credential
                                            if (credential is GoogleIdTokenCredential) {
                                                viewModel.signInWithGoogle(credential.idToken) {
                                                    navController.navigate(Screen.Home.route) {
                                                        popUpTo(Screen.Welcome.route) { inclusive = true }
                                                    }
                                                }
                                            }
                                        } catch (e: GetCredentialException) {
                                            Toast.makeText(context, "$googleFailedMsg: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                            SocialAuthButton(
                                text = stringResource(R.string.facebook),
                                icon = painterResource(id = R.drawable.ic_facebook),
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    LoginManager.getInstance().logOut()
                                    LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY)
                                    LoginManager.getInstance().logInWithReadPermissions(
                                        context as androidx.activity.ComponentActivity,
                                        callbackManager,
                                        listOf("email", "public_profile")
                                    )
                                }
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
    }
}
