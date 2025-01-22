package com.example.diplom

//import io.github.jan.supabase.storage.storage
//import io.github.jan.supabase.storage.upload
import android.content.ContentResolver
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@Composable
fun Profile(contentResolver: ContentResolver) {
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
//        Pfp(contentResolver)

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

//@Composable
//fun Pfp(contentResolver: ContentResolver) {
//
//    val context = LocalContext.current
//    val lifecycleOwner = rememberUpdatedState(context as LifecycleOwner)
//
//    val image = rememberSaveable() { mutableStateOf("") }
//
//
//    val painter = rememberImagePainter(
//        if (image.value.isEmpty()) {
//            R.drawable.person
//        } else {
//            image.value
//        }
//    )
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//
//    }
//    val launcher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let { image.value = it.toString() }
//            if(uri!=null){
//                lifecycleOwner.value.lifecycleScope.launch {
//                    uploadFile(uri, lifecycleOwner, contentResolver)
//                }
//            }
//        }
//    Column(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Card(
//            shape = CircleShape, modifier = Modifier
//                .padding(8.dp)
//                .size(200.dp)
//        ) {
//            Image(
//                painter = painter,
//                contentDescription = null,
//                modifier = Modifier
//                    .wrapContentSize()
//                    .clickable { launcher.launch("image/*") },
//                contentScale = ContentScale.Crop,
//            )
//        }
//        Text(text = "Change profile picture")
//    }
//
//
//}
//private suspend fun uploadFile(uri: Uri, lifecycleOwner:State<LifecycleOwner>, contentResolver: ContentResolver) {
//    val inputStream = contentResolver.openInputStream(uri)
//    val bytes = inputStream?.readBytes()
//    val bucket = supabase.storage.from("pavel")
//    uri.path?.let {
//        if (bytes != null) {
//            bucket.upload(it,bytes)
//        }
//    }
//}
//private fun uriToByteArray(contentResolver: ContentResolver, uri: Uri): ByteArray {
//    if (uri == Uri.EMPTY) {
//        return byteArrayOf()
//    }
//    val inputStream = contentResolver.openInputStream(uri)
//    if (inputStream != null) {
//        return getBytes(inputStream)
//    }
//    return byteArrayOf()
//}
//
//private fun getBytes(inputStream: InputStream): ByteArray {
//    val byteBuffer = ByteArrayOutputStream()
//    val bufferSize = 1024
//    val buffer = ByteArray(bufferSize)
//    var len = 0
//    while (inputStream.read(buffer).also { len = it } != -1) {
//        byteBuffer.write(buffer, 0, len)
//    }
//    return byteBuffer.toByteArray()
//}


