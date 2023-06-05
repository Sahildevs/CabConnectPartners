package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class AddVehicleRequest(

    @SerializedName("model")
    var model: String? = null,

    @SerializedName("number")
    var number: String? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("drivers_id")
    var drivers_id: Int? = null,

    @SerializedName("location")
    var location: LocationData? = null,

    @SerializedName("state")
    var state: VehicleStatus
)
