package com.example.uberdrive.ui.landing

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LandingBaseViewModel @Inject constructor(): ViewModel() {

    /** Driver details */
    var name: String? = null
    var phone: String? = null
    var driverId: Int? = null
    var vehicleName: String? = null
    var vehicleNo: String? = null
    var vehicleId: Int? = null
}