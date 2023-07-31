package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class AddVehicleResponse(

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("model")
    var model: String? = null,

    @SerializedName("number")
    var number: String? = null
)
