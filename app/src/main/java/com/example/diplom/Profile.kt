package com.example.diplom

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun Profile() {
    val note = rememberSaveable() { mutableStateOf("") }
    if (note.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, note.value, Toast.LENGTH_SHORT).show()
        note.value = ""
    }

    var name by rememberSaveable { mutableStateOf("your name") }
    var username by rememberSaveable { mutableStateOf("your name") }
    var bio by rememberSaveable { mutableStateOf("your name") }


    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "cancel", modifier = Modifier.clickable { note.value = "Cancelled" })
            Text(text = "Save", modifier = Modifier.clickable { note.value = "Saved" })
        }
        Pfp()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name ", modifier = Modifier.width(100.dp))
            TextField(value = name, onValueChange = { name = it })
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "UserName ", modifier = Modifier.width(100.dp))
            TextField(value = username, onValueChange = { username = it })
        }
    }
}

//fun imageToBase64(uri: Uri, contentResolver: ContentResolver): String {
//    val inputStream = contentResolver.openInputStream(uri)
//    val bytes = inputStream?.readBytes()
//    return bytes?.let {
//        Base64.encodeToString(it, Base64.DEFAULT)
//    } ?: ""
//}

//val cv = LocalContext.current.contentResolver
//
//    data class Pfp(
//        val key:String="",
//        val name:String="",
//        val description:String="",
//        val price:String="",
//        val category:String="",
//        val IMGUrl:String="",
//    )
//    fun saveToFireStore(
//
//        firestore: FirebaseFirestore,
//        url: String,
//        pfp: Pfp
//
//        ) {
//        val db = firestore.collection("UserPfp")
//        val key = db.document().id
//
//        db.document(key).set(pfp.copy(key=key,IMGUrl=url))
//            .addOnCompleteListener{
//
//            }
//            .addOnFailureListener{
//
//            }
//    }

@Composable
fun Pfp() {


    val image = rememberSaveable() { mutableStateOf("") }


    val painter = rememberImagePainter(
        if (image.value.isEmpty()) {
            R.drawable.person
        } else {
            image.value
            //запихнуть все в бд

        }

    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { image.value = it.toString() }
        }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(200.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop,
            )
        }
        Text(text = "Change profile picture")
//        Button(onClick = {
//            saveToFireStore(
//                firestore = ,
//                url = ,
//                pfp =
//            )
//        }) {}

    }


}