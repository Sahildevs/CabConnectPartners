package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class GetTripDetailsResponse(

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("pickup_location")
    var pickUpLocation: LocationData,

    @SerializedName("drop_location")
    var dropLocation: LocationData,

    @SerializedName("user")
    var riderDetails: RiderData

)
