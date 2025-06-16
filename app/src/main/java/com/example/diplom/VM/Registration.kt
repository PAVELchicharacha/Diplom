package com.example.diplom.VM

import android.util.Patterns
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.yandex.mapkit.mapview.MapView


@Composable
fun Map(mapView: MapView) {
    AndroidView(
        factory = { mapView },
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth(),
    )
}

@Composable
fun AuthScreen(onAuth: () -> Unit, mapView: MapView) {
    val auth = Firebase.auth

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Map(mapView = mapView)

        TextField(
            value = email.value,
            onValueChange = {
                email.value = it
                isEmailValid = it.isNotEmpty() && isValidEmail(it)
            },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (!isEmailValid) Color.Red else Color.Black,
                    shape = RoundedCornerShape(100.dp)
                )
                .fillMaxWidth(0.8f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),
            label = { Text("Email") },
            isError = !isEmailValid
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = password.value,
            onValueChange = {
                password.value = it
                isPasswordValid = it.isNotEmpty() && it.length >= 6
            },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (!isPasswordValid) Color.Red else Color.Black,
                    shape = RoundedCornerShape(100.dp)
                )
                .fillMaxWidth(0.8f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = !isPasswordValid
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                isEmailValid = email.value.isNotEmpty() && isValidEmail(email.value)
                isPasswordValid = password.value.isNotEmpty() && password.value.length >= 6

                if (isEmailValid && isPasswordValid) {
                    signIn(
                        auth, email.value, password.value,
                        onSuccess = onAuth,
                        onError = { message ->
                            errorMessage = message
                        }
                    )
                } else {
                    errorMessage = "Проверьте правильность введенных данных"
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(text = "Sign In")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                isEmailValid = email.value.isNotEmpty() && isValidEmail(email.value)
                isPasswordValid = password.value.isNotEmpty() && password.value.length >= 6

                if (isEmailValid && isPasswordValid) {
                    signUp(
                        auth, email.value, password.value,
                        onSuccess = onAuth,
                        onError = { message ->
                            errorMessage = message
                        }
                    )
                } else {
                    errorMessage = "Проверьте правильность введенных данных"
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(text = "Sign Up")
        }
    }
}

private fun signIn(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Ошибка входа")
            }
        }
}

private fun signUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Ошибка регистрации")
            }
        }
}