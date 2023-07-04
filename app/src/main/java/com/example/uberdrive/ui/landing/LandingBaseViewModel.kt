package com.example.uberdrive.ui.landing

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uberdrive.utils.FirebaseUtils
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

    /** Vehicle location details */
    var currentLat: String? = null
    var currentLng: String? = null

    var isLive: Boolean = false
    private val firebaseUtils = FirebaseUtils()

    private var _responseGoLive = MutableLiveData<String>()
    val responseGoLive: LiveData<String> = _responseGoLive

    private var _responseGoOffline = MutableLiveData<String>()
    val responseGoOffline: LiveData<String> = _responseGoOffline



    /** Add active driver firebase service call */
    fun goLive() {
        val map = mutableMapOf<String, String>()
        map["Lat"] = currentLat!!
        map["Lng"] = currentLng!!

        firebaseUtils.addActiveDriver(
            map = map,
            driverId = driverId.toString(),
            onSuccess = {
                _responseGoLive.postValue(it)
            },
            onFailure = {
                _responseGoLive.postValue(it)
            }
        )

    }

    /** Remove active driver firebase service call */
    fun goOffline() {
        if (isLive) {
            firebaseUtils.removeActiveDriver(
                driverId = driverId.toString(),
                onSuccess = {
                    _responseGoOffline.postValue(it)
                },
                onFailure = {
                    _responseGoOffline.postValue(it)
                }
            )
            isLive = false
        }

    }

    /** Update the lat lng of the active driver */
    fun updateActiveDriverData() {
        val map = mutableMapOf<String, String>()
        map["Lat"] = currentLat!!
        map["Lng"] = currentLng!!

        firebaseUtils.updateActiveDriver(
            driverId = driverId.toString(),
            map = map
        )
    }
}