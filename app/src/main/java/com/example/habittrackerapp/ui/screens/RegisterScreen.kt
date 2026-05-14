package com.example.habittrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.habittrackerapp.R
import com.example.habittrackerapp.navigation.Screen
import com.example.habittrackerapp.ui.theme.EmeraldGreen
import com.example.habittrackerapp.ui.theme.LightGrayBorder

@Composable
fun RegisterScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.create_account),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = stringResource(R.string.start_building_better_habits),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                InputField(label = stringResource(R.string.username_label), placeholder = stringResource(R.string.username_hint), icon = Icons.Default.Person)
                Spacer(modifier = Modifier.height(16.dp))
                InputField(label = stringResource(R.string.email_address_full), placeholder = stringResource(R.string.email_address_hint), icon = Icons.Default.Email)
                Spacer(modifier = Modifier.height(16.dp))
                InputField(label = stringResource(R.string.password_label), placeholder = "••••••••", icon = Icons.Default.Lock)
                Spacer(modifier = Modifier.height(16.dp))
                InputField(label = stringResource(R.string.confirm_password_label), placeholder = "••••••••", icon = Icons.Default.History)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate(Screen.Home.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(text = stringResource(R.string.register), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(text = "  " + stringResource(R.string.or_divider) + "  ", fontSize = 12.sp, color = Color.Gray)
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SocialButton(text = stringResource(R.string.google), icon = Icons.Default.GTranslate, modifier = Modifier.weight(1f))
                    SocialButton(text = stringResource(R.string.facebook), icon = Icons.Default.Facebook, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { navController.navigate(Screen.Home.route) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(text = stringResource(R.string.continue_as_guest), color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().border(1.dp, LightGrayBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            TextButton(onClick = { navController.navigate(Screen.Login.route) }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.already_have_account_login), color = Color(0xFF1976D2))
            }
        }
    }
}

@Composable
fun InputField(label: String, placeholder: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            leadingIcon = { Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreen, unfocusedBorderColor = LightGrayBorder)
        )
    }
}

@Composable
fun SocialButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    OutlinedButton(
        onClick = {},
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(24.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp)
    }
}
