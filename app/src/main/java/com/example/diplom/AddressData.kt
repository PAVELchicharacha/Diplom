package com.example.diplom

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class AddressData(
    val id:Int,
    val address:String,
    val equipment: String,
    val description: String
)
{
    constructor():this(0,"","","")
}