package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class RespondToTripRequestResponse(

    @SerializedName("state")
    var state: String? = null
)
