package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class LocationData(

    @SerializedName("lat")
    val lat: Double? = null,

    @SerializedName("lng")
    val lng: Double? = null,

)
