package com.example.finalpro.Ui1.Screens.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalpro.Ui1.Theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    val state        by vm.state.collectAsState()
    var email        by remember { mutableStateOf("") }
    var pass         by remember { mutableStateOf("") }
    var passVisible  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.resetState() }

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            vm.resetState()
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(BgPrimary)) {
        Box(
            modifier = Modifier
                .size(350.dp)
                .offset(x = (-60).dp, y = (-100).dp)
                .background(
                    brush = Brush.radialGradient(colors = listOf(AccentGlow, Color.Transparent)),
                    shape = CircleShape
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            verticalArrangement     = Arrangement.Center,
            horizontalAlignment     = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(AccentGlow)
                    .border(1.5.dp, AccentPrimary, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.AccountBalanceWallet, null,
                    tint     = AccentPrimary,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(Modifier.height(20.dp))
            Text("FinanceApp",
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("Tus finanzas, bajo control",
                style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Spacer(Modifier.height(40.dp))

            OutlinedTextField(
                value         = email,
                onValueChange = { email = it },
                label         = { Text("Correo electrónico") },
                leadingIcon   = { Icon(Icons.Rounded.Email, null, tint = TextSecondary) },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                colors   = financeTextFieldColors()
            )
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value         = pass,
                onValueChange = { pass = it },
                label         = { Text("Contraseña") },
                leadingIcon   = { Icon(Icons.Rounded.Lock, null, tint = TextSecondary) },
                trailingIcon  = {
                    IconButton(onClick = { passVisible = !passVisible }) {
                        Icon(
                            if (passVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            null, tint = TextSecondary
                        )
                    }
                },
                visualTransformation = if (passVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                colors   = financeTextFieldColors()
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick  = { vm.login(email, pass) },
                enabled  = state !is AuthState.Loading,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = AccentPrimary)
            ) {
                if (state is AuthState.Loading)
                    CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                else
                    Text("Iniciar sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            if (state is AuthState.Error) {
                Spacer(Modifier.height(12.dp))
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ColorGasto.copy(alpha = 0.1f)),
                    shape  = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        (state as AuthState.Error).msg,
                        color    = ColorGasto,
                        modifier = Modifier.padding(12.dp),
                        style    = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            TextButton(onClick = onGoToRegister) {
                Text("¿No tienes cuenta? ", color = TextSecondary)
                Text("Regístrate", color = AccentPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun financeTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = AccentPrimary,
    unfocusedBorderColor    = Border,
    focusedLabelColor       = AccentPrimary,
    cursorColor             = AccentPrimary,
    focusedTextColor        = TextPrimary,
    unfocusedTextColor      = TextPrimary,
    unfocusedLabelColor     = TextSecondary,
    focusedContainerColor   = BgCard,
    unfocusedContainerColor = BgCard
)