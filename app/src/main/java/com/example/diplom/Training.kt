package com.example.diplom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.diplom.ui.theme.DiplomTheme

@Composable
fun Training() {
    DiplomTheme(
    ) {
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
                ) { index ->
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