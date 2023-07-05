package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class UpdateVehicleResponse(

    @SerializedName("state")
    var state: VehicleStatus
)
