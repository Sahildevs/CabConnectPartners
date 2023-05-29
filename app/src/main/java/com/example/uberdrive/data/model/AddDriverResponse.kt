package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class AddDriverResponse(

    @SerializedName("driver")
    var driver: DriverData
)
