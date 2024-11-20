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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.diplom.ui.theme.DiplomTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth
        setContent {
            MyApp(auth)
//            MyImageScreen()
//            Training()

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
                        Chat()
                    }
                    "Profile"-> {
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


@Composable
fun AuthScreen(onAuth: () -> Unit) {


    val auth = Firebase.auth

    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    Log.d("mylog", "user email:${auth.currentUser?.email}")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    //ввод почты и пароля
    {
        TextField(value = email.value, onValueChange = {
            email.value = it
        })
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = password.value, onValueChange = {
            password.value = it
        })
        Button(onClick = {
            SignIn(auth, email.value, password.value)
            onAuth()
        }) {
            Text(text = "Sign In")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            SignUp(auth, email.value, password.value)
        }) {
            Text(text = "Sign Up")
        }
    }
}
@Composable
fun Register(){

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

@Composable
fun Training(){
    DiplomTheme(
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            val imageId = arrayOf(
                R.drawable.arms,
                R.drawable.leg,
                R.drawable.press,
                R.drawable.back,
                R.drawable.chest,
                R.drawable.neck,
            )

            val names = arrayOf(
                "упражнения на руки",
                "упражнения на ноги",
                "упражнения на пресс",
                "упражнения на спину",
                "упражнения на грудь",
                "упражнения на шею"
            )

            val ingredients = arrayOf(
                "Tomato sos, cheese, oregano, peperoni",
                "Tomato sos, cheese, oregano, spinach, green paprika, rukola",
                "Tomato sos, oregano, mozzarella, goda, parmesan, cheddar",
                "Tomato sos, cheese, oregano, bazillion",
                "Tomato sos, cheese, oregano, green paprika, red beans",
                "Tomato sos, cheese, oregano, corn, jalapeno, chicken",

            )
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "MainScreen") {
                composable(route = "MainScreen") {
                    TrainScreen(imageId, names, ingredients, navController)
                }
                composable(route = "DetailScreen/{index}",
                    arguments = listOf(
                        navArgument(name = "index") {
                            type = NavType.IntType
                        }
                    )
                ) { index->
                    DetailScreen(
                        photos = imageId,
                        names = names,
                        ingredients = ingredients,
                        itemIndex = index.arguments?.getInt("index")
                    )
                }
            }
        }
    }
}








@Composable
fun Chat(){
    Text("qweqweqwe2")
}
@Composable
fun Profile(){
    Text("qweqweqwe3")
}
