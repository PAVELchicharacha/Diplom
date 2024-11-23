package com.example.diplom

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AuthScreen(onAuth: () -> Unit) {
    val auth = Firebase.auth

    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    Log.d("my log", "user email:${auth.currentUser?.email}")


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    //ввод почты и пароля
    {
        TextField(
            value = email.value, onValueChange = { email.value = it },

            modifier = Modifier.border(
                brush = Brush.horizontalGradient(colors = listOf(Color.Black, Color.Black)),
                shape = RoundedCornerShape(100.dp), width = 1.dp
            ),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),

            )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = password.value, onValueChange = { password.value = it },

            modifier = Modifier.border(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Black
                    )
                ), shape = RoundedCornerShape(100.dp), width = 1.dp
            ),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            )
        )
        Button(
            onClick = {
                SignIn(auth, email.value, password.value)
                onAuth()
            },
            colors = ButtonColors(Color.Cyan, Color.Black, Color.Black, Color.Black),
        ) {
            Text(text = "Sign In")
        }
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { SignUp(auth, email.value, password.value) },
            colors = ButtonColors(Color.Cyan, Color.Black, Color.Black, Color.Black)
        )
        {
            Text(text = "Sign Up")
        }
    }
}