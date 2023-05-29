package com.example.uberdrive.data.model

import com.google.gson.annotations.SerializedName

data class AddDriverRequest(

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("phone")
    var phone: String? = null,

    @SerializedName("license_no")
    var license: String? = null
)
