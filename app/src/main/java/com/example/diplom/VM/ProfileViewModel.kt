package com.example.diplom.VM

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.diplom.DATA.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance().reference

    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")
    val profileImageUrl = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        isLoading.value = true

        db.child("users").child(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profile = snapshot.getValue(UserProfile::class.java)
                    profile?.let {
                        firstName.value = it.firstName
                        lastName.value = it.lastName
                        profileImageUrl.value = it.profileImageUrl
                    }
                    isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    errorMessage.value = "Ошибка загрузки профиля"
                    isLoading.value = false
                }
            }
        )
    }

    fun updateProfile() {
        val userId = auth.currentUser?.uid ?: return
        isLoading.value = true

        val userProfile = UserProfile(
            userId = userId,
            firstName = firstName.value,
            lastName = lastName.value,
            profileImageUrl = profileImageUrl.value
        )

        db.child("users").child(userId).setValue(userProfile)
            .addOnSuccessListener {
                errorMessage.value = ""
                isLoading.value = false
            }
            .addOnFailureListener {
                errorMessage.value = "Ошибка сохранения"
                isLoading.value = false
            }
    }

    fun uploadProfileImage(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        isLoading.value = true

        val imageRef = storage.child("profile_images/$userId/${UUID.randomUUID()}")
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    profileImageUrl.value = uri.toString()
                    updateProfile()
                }
            }
            .addOnFailureListener {
                errorMessage.value = ""
                isLoading.value = false
            }
    }
}