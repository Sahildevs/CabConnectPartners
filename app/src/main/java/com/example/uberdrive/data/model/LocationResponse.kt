package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class LocationResponse(

    @SerializedName("data")
    var data: LocationData
)
