package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class DriverData(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("phone")
    val phone: String

)
