package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class RiderData(

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("phone")
    var phone: String? = null
)
