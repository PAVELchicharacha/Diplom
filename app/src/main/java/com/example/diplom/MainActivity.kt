package com.example.diplom
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
import com.example.diplom.M.AddressScreen
import com.example.diplom.M.Profile
import com.example.diplom.M.ProfileScreen
import com.example.diplom.M.TrainingScreen
import com.example.diplom.V.ShitChatScreen
import com.example.diplom.VM.AuthScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView

class MainActivity : ComponentActivity() {
    lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth

        MapKitFactory.setApiKey("ece82d35-4eca-4566-ba3f-4f942bf62d85")
        MapKitFactory.initialize(this)
        mapView = MapView(this)
        Firebase.database.setPersistenceEnabled(true)
        setContent{
//            ProfileScreen()
            MyApp(auth, mapView)
//            TrainingScreen()
//            MyImageScreen()
//            Training()
//            Profile()
        }
    }
}

@Composable
fun MyApp(auth: FirebaseAuth,mapView: MapView) {
    var isRegistered by remember { mutableStateOf(false) }
    if (isRegistered) {
        MainScreen(auth)
    } else {
        AuthScreen(onAuth = { isRegistered = true },mapView)
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
                        TrainingScreen()
                    }

                    "Chat" -> {
                        ShitChatScreen()
                    }

                    "Profile" -> {
                        ProfileScreen()
                    }
                    "Address"->{
                        AddressScreen()
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
                Image(
                    painter = painterResource(id = R.drawable.ping),
                    modifier = Modifier.clickable { route = "Address" },
                    contentDescription = "Address"
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