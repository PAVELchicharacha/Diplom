package com.example.diplom

import androidx.compose.runtime.Composable
import com.example.diplom.VM.AuthScreen
import org.junit.Test

import org.junit.Assert.*
import com.yandex.mapkit.mapview.MapView
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
lateinit var mapView: MapView

class ExampleUnitTest {
    @Test
    fun regisration(){
        val result = IsValid("PavelKiprov@yandex.ru","qwerty")
        assertTrue(result)
    }
    @Test
    fun regisrationWithOutLogin(){
        val result = IsValid("","qwerty")
        assertTrue(result)
    }
    @Test
    fun regisrationWithOutPassword(){
        val result = IsValid("PavelKiprov@yandex.ru","")
        assertTrue(result)
    }
    private fun IsValid(Login:String,Password:String):Boolean{
        return Login.isNotEmpty()&& Password.isNotEmpty()
    }

}