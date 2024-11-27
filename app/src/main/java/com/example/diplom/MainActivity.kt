package com.example.diplom

// Основной импорт для Firestore

// Импорт для работы с документами
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


val supabase = createSupabaseClient(
    supabaseUrl = "https://pzbqhunebwbqivfqojom.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB6YnFodW5lYndicWl2ZnFvam9tIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzI2NTI2MjcsImV4cCI6MjA0ODIyODYyN30.jzstnGKtXgNtGJuAYzT5BrKm1CI7qTlo-wSABzNEZlw"
) {
    install(Postgrest)
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth



        setContent{
            MyApp(auth)
//            MyImageScreen()
//            Training()
//            Profile()
        }
    }
}

@Composable
fun MyApp(auth: FirebaseAuth) {
    var isRegistered by remember { mutableStateOf(false) }
    if (isRegistered) {
        MainScreen(auth)
    } else {
        AuthScreen(onAuth = { isRegistered = true })
    }
}

@Composable
fun MainScreen(auth: FirebaseAuth) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            var route by remember { mutableStateOf("home") }
            Box(
                modifier = Modifier
                    .weight(1f)
            )
            //при нажатии на надпись идет переход на нужный экран
            {
                when (route) {
                    "Home" -> {
                        Training()
                    }

                    "Chat" -> {
                        ChatScreen()
                    }

                    "Profile" -> {
                        Profile()
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.home),
                    modifier = Modifier.clickable { route = "Home" },
                    contentDescription = "Home"
                )
                Image(
                    painter = painterResource(id = R.drawable.chat),
                    modifier = Modifier.clickable { route = "Chat" },
                    contentDescription = "chat"
                )
                Image(
                    painter = painterResource(id = R.drawable.user),
                    modifier = Modifier.clickable { route = "Profile" },
                    contentDescription = "Profile"
                )
            }
        }
    }
}
//выход из аккаунта
fun SignOut(auth: FirebaseAuth) {
    auth.signOut()
}

//удаление аккауна
fun DeleteAccount(auth: FirebaseAuth) {
    auth.currentUser?.delete()!!
    Log.d("myLog", "delete is successfull")
}

//вход в аккаунт
fun SignIn(auth: FirebaseAuth, email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
        if (it.isSuccessful) {
            Log.d("myLog", "Sign up successfull")
        } else {
            Log.d("myLog", "Sign up failure")
        }
    }
}

//регистация аккаунта
fun SignUp(auth: FirebaseAuth, email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
        if (it.isSuccessful) {
            Log.d("myLog", "Sign up successfull")
        } else {
            Log.d("myLog", "Sign up failure")
        }
    }
}