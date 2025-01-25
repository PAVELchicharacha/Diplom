package com.example.diplom

//import io.github.jan.supabase.storage.storage
//import io.github.jan.supabase.storage.upload
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
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
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.serialization.gson.gson
import io.ktor.util.InternalAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream

suspend fun getLinkFromResponse(response: HttpResponse): String? {
    val jsonResponse = response.bodyAsText()
    return try {
        val jsonObject = JSONObject(jsonResponse)
        val dataObject = jsonObject.getJSONObject("data")
        dataObject.getString("link")
    } catch (e: Exception) {
        Log.e("my log", "Error parsing response: ${e.message}")
        null
    }
}
fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        // 1. Получаем InputStream из URI
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.use { stream ->
            // 2. Читаем данные из InputStream в массив байтов
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (stream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
            val byteArray = byteArrayOutputStream.toByteArray()

            // 3. Кодируем массив байтов в Base64
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getFileName(context: Context, uri: Uri): String? {
    var fileName: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                fileName = it.getString(displayNameIndex)
            }
        }
    }
    return fileName
}

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson()
    }
}

@OptIn(InternalAPI::class)
suspend fun Response(uri: ByteArray, mimeType: String, fileName: String) {
    val response: HttpResponse =
        client.post("https://api.imgur.com/3/image") {
            headers {
                append("Authorization", "Client-ID 3fff6a5e315775d")
            }
            body = MultiPartFormDataContent(
                formData {
                    append(
                        "image",
                        uri,
                        io.ktor.http.Headers.build {
                            append(HttpHeaders.ContentType, mimeType)
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        }
                    )
                }
            )
        }

    Log.d("my log", "response:${getLinkFromResponse(response)}")
}

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

@OptIn(InternalAPI::class)
@Composable
fun Pfp() {
    val context = LocalContext.current
    val image = rememberSaveable() { mutableStateOf("") }
    val mimeType = rememberSaveable() { mutableStateOf<String?>("") }
    val fileName = rememberSaveable() { mutableStateOf<String?>("") }
    val base64Image = rememberSaveable() { mutableStateOf<String?>("") }

    val painter = rememberImagePainter(
        if (image.value.isEmpty()) {
            R.drawable.person
        } else {
            image.value
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
            uri?.let {
                base64Image.value = uriToBase64(context, it)
                image.value = it.toString()
                mimeType.value = context.contentResolver.getType(it)
                fileName.value = getFileName(context, it)

                val inputStream = context.contentResolver.openInputStream(uri)
                val fileBytes = inputStream?.use { it.readBytes() }

                var ignat = ImageObject(
                    uri = fileBytes!!,
                    mimeType = mimeType.value,
                    fileName = fileName.value
                )
                if (base64Image.value != null && mimeType.value != null && fileName.value != null) {
                    // Используем CoroutineScope для вызова suspend-функции
                    CoroutineScope(Dispatchers.IO).launch {

                        Response(ignat.uri!!, ignat.mimeType!!, ignat.fileName!!)

                        Log.d("myLog", "uri.value:${ignat.uri}")
                        Log.d("myLog", "mimeType.value:${ignat.mimeType}")
                        Log.d("myLog", "fileName.value:${ignat.fileName}")

                        Log.d("myLog", "uri:${uri}")


                    }
                }

            }
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
                    .fillMaxSize()
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop,
            )
        }
        Text(text = "Change profile picture")

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


