package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class GetTripDetailsResponse(

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("pickup_location")
    var pickUpLocation: LocationResponse,

    @SerializedName("drop_location")
    var dropLocation: LocationResponse,

    @SerializedName("user")
    var riderDetails: RiderData

)
