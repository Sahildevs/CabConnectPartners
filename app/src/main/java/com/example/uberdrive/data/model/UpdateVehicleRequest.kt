package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class UpdateVehicleRequest(

    @SerializedName("cars_id")
    var cars_id: Int? = null,

    @SerializedName("location")
    var location: LocationData,

    @SerializedName("state")
    var state: VehicleStatus
)
