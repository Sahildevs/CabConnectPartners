package com.example.uberdrive.ui.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uberdrive.data.model.GetTripDetailsResponse
import com.example.uberdrive.data.model.LocationData
import com.example.uberdrive.data.model.UpdateVehicleRequest
import com.example.uberdrive.data.model.UpdateVehicleResponse
import com.example.uberdrive.data.model.VehicleStatus
import com.example.uberdrive.data.repository.MainRepository
import com.example.uberdrive.utils.FirebaseUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LandingBaseViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    /** Driver details */
    var name: String? = null
    var phone: String? = null
    var driverId: Int? = null
    var vehicleName: String? = null
    var vehicleNo: String? = null
    var vehicleId: Int? = null

    /** Vehicle location details */
    var currentLat: Double? = null
    var currentLng: Double? = null

    /** Trip request details */
    var tripId: Int? = null
    var customerName: String? = null
    var customerPhone: String? = null
    var pickUpLat: Double? = null
    var pickUpLng: Double? = null
    var pickUpAddress: String? = null
    var dropLat: Double? = null
    var dropLng: Double? = null
    var dropAddress: String? = null

    var isRideRequestBottomSheetVisible: Boolean = false



    var isLive: Boolean = false
    private val firebaseUtils = FirebaseUtils()

    private var _responseGoLive = MutableLiveData<String>()
    val responseGoLive: LiveData<String> = _responseGoLive

    private var _responseGoOffline = MutableLiveData<String>()
    val responseGoOffline: LiveData<String> = _responseGoOffline

    private var _responseUpdateVehicleServiceCall = MutableLiveData<Response<UpdateVehicleResponse>>()
    val responseUpdateVehicleServiceCall: LiveData<Response<UpdateVehicleResponse>> = _responseUpdateVehicleServiceCall

    private var _responseGetTripDetailsServiceCall = MutableLiveData<Response<GetTripDetailsResponse>>()
    val responseGetTripDetailsServiceCall: LiveData<Response<GetTripDetailsResponse>> = _responseGetTripDetailsServiceCall



    /** Add active driver firebase service call */
    fun goLive() {
        val map = mutableMapOf<String, String>()
        map["Lat"] = currentLat.toString()
        map["Lng"] = currentLng.toString()

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

    /** Update active driver lat lng firebase service call */
    fun updateActiveDriverData() {
        val map = mutableMapOf<String, String>()
        map["Lat"] = currentLat.toString()
        map["Lng"] = currentLng.toString()

        firebaseUtils.updateActiveDriver(
            driverId = driverId.toString(),
            map = map
        )

    }

    /** Update vehicle details service call */
    suspend fun updateVehicleServiceCall(status: VehicleStatus) {
        val request = UpdateVehicleRequest(
            cars_id = vehicleId,
            location = LocationData(lat = currentLat, lng = currentLng),
            state = status
        )

        val res = repository.updateVehicle(vehicleId!!, request)
        _responseUpdateVehicleServiceCall.postValue(res)

    }

    suspend fun getTripDetails() {
        val request = vehicleId!!

        val res = repository.getTripDetails(request)
        _responseGetTripDetailsServiceCall.postValue(res)
    }

    /** Update trip request request status firebase service call */
    fun updateRideRequestStatus(status: String) {
        val map = mutableMapOf<String, String>()
        map["status"] = status

        firebaseUtils.updateRideRequestStatus(
            driverId = driverId.toString(),
            map = map
        )
    }

}