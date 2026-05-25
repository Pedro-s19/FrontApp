package com.example.finalpro.Ui1.Screens.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state   by viewModel.state.collectAsState()
    var email   by remember { mutableStateOf("") }
    var pass    by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var passVis by remember { mutableStateOf(false) }

    // Validaciones locales en tiempo real
    val isEmailValid = remember(email) {
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    val isPasswordValid = remember(pass) {
        pass.length >= 8 &&
                pass.any { it.isUpperCase() } &&
                pass.any { it.isDigit() } &&
                pass.any { "!@#$%^&*(),.?\":{}|<>".contains(it) }
    }
    val passwordsMatch = pass == confirm

    LaunchedEffect(state) {
        if (state is RegisterState.Success) {
            viewModel.resetState()
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón volver
            Row(Modifier.fillMaxWidth()) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Rounded.ArrowBack, null, tint = TextPrimary)
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Crear cuenta",
                style      = MaterialTheme.typography.headlineMedium,
                color      = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Empieza a controlar tus finanzas",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(Modifier.height(32.dp))

            // Email
            OutlinedTextField(
                value         = email,
                onValueChange = { email = it },
                label         = { Text("Correo electrónico") },
                leadingIcon   = { Icon(Icons.Rounded.Email, null, tint = TextSecondary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError       = email.isNotEmpty() && !isEmailValid,
                supportingText = {
                    if (email.isNotEmpty() && !isEmailValid) {
                        Text("Ingresa un correo válido (ejemplo@dominio.com)", color = ColorGasto)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                colors   = financeTextFieldColors()
            )
            Spacer(Modifier.height(14.dp))

            // Contraseña
            OutlinedTextField(
                value         = pass,
                onValueChange = { pass = it },
                label         = { Text("Contraseña (mín. 8 caracteres)") },
                leadingIcon   = { Icon(Icons.Rounded.Lock, null, tint = TextSecondary) },
                trailingIcon  = {
                    IconButton(onClick = { passVis = !passVis }) {
                        Icon(
                            if (passVis) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            null, tint = TextSecondary
                        )
                    }
                },
                visualTransformation = if (passVis) VisualTransformation.None
                else PasswordVisualTransformation(),
                isError       = pass.isNotEmpty() && !isPasswordValid,
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                colors   = financeTextFieldColors()
            )

            // Requisitos de contraseña visibles mientras se escribe
            if (pass.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Column(modifier = Modifier.fillMaxWidth().padding(start = 4.dp)) {
                    Text("La contraseña debe:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    RequirementRow("Mínimo 8 caracteres", pass.length >= 8)
                    RequirementRow("Al menos una mayúscula", pass.any { it.isUpperCase() })
                    RequirementRow("Al menos un número", pass.any { it.isDigit() })
                    RequirementRow("Al menos un carácter especial", pass.any { "!@#$%^&*(),.?\":{}|<>".contains(it) })
                }
            }

            Spacer(Modifier.height(14.dp))

            // Confirmar contraseña
            OutlinedTextField(
                value         = confirm,
                onValueChange = { confirm = it },
                label         = { Text("Confirmar contraseña") },
                leadingIcon   = { Icon(Icons.Rounded.Lock, null, tint = TextSecondary) },
                visualTransformation = PasswordVisualTransformation(),
                isError  = confirm.isNotEmpty() && !passwordsMatch,
                supportingText = {
                    if (confirm.isNotEmpty() && !passwordsMatch) {
                        Text("Las contraseñas no coinciden", color = ColorGasto)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                colors   = financeTextFieldColors()
            )

            Spacer(Modifier.height(24.dp))

            // Botón registrar
            Button(
                onClick  = {
                    if (isEmailValid && isPasswordValid && passwordsMatch) {
                        viewModel.register(email, pass)
                    }
                },
                enabled  = state !is RegisterState.Loading
                        && email.isNotBlank()
                        && pass.isNotEmpty()
                        && confirm.isNotEmpty()
                        && isEmailValid
                        && isPasswordValid
                        && passwordsMatch,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = AccentPrimary)
            ) {
                if (state is RegisterState.Loading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(22.dp),
                        color       = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Crear cuenta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            // Mensaje de error del servidor
            if (state is RegisterState.Error) {
                Spacer(Modifier.height(12.dp))
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ColorGasto.copy(alpha = 0.1f)),
                    shape  = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text     = (state as RegisterState.Error).msg,
                        color    = ColorGasto,
                        modifier = Modifier.padding(12.dp),
                        style    = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onBack) {
                Text("¿Ya tienes cuenta? ", color = TextSecondary)
                Text("Inicia sesión", color = AccentPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RequirementRow(text: String, satisfied: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            if (satisfied) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
            null,
            tint = if (satisfied) ColorIngreso else ColorGasto,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = if (satisfied) ColorIngreso else TextMuted)
    }
}