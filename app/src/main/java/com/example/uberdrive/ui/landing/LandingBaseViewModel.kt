package com.example.uberdrive.ui.landing

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uberdrive.data.model.RespondToTripRequestResponse
import com.example.uberdrive.data.model.GetTripDetailsResponse
import com.example.uberdrive.data.model.LocationData
import com.example.uberdrive.data.model.UpdateVehicleRequest
import com.example.uberdrive.data.model.UpdateVehicleResponse
import com.example.uberdrive.data.model.VehicleStatus
import com.example.uberdrive.data.repository.MainRepository
import com.example.uberdrive.utils.FirebaseUtils
import com.example.uberdrive.utils.SharedPrefUtils
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LandingBaseViewModel @Inject constructor(private val repository: MainRepository, private val sharedPrefUtils: SharedPrefUtils): ViewModel() {

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

    var isLive: Boolean = false
    var isAvailable: Boolean = false
    var isRequestAccepted: Boolean = false
    var isCabArrivedAtPickup: Boolean = false
    var isCabArrivedAtDrop: Boolean = false

    var initialCabLocation: LatLng? = null
    var currentCabLocation: LatLng? = null

    private val firebaseUtils = FirebaseUtils()

    private var _responseGoLive = MutableLiveData<String>()
    val responseGoLive: LiveData<String> = _responseGoLive

    private var _responseGoOffline = MutableLiveData<String>()
    val responseGoOffline: LiveData<String> = _responseGoOffline

    private var _responseUpdateVehicleServiceCall = MutableLiveData<Response<UpdateVehicleResponse>>()
    val responseUpdateVehicleDataServiceCall: LiveData<Response<UpdateVehicleResponse>> = _responseUpdateVehicleServiceCall

    private var _responseGetTripDetailsServiceCall = MutableLiveData<Response<GetTripDetailsResponse>>()
    val responseGetTripDetailsServiceCall: LiveData<Response<GetTripDetailsResponse>> = _responseGetTripDetailsServiceCall

    private var _responseDeclineTripRequestServiceCall = MutableLiveData<Response<RespondToTripRequestResponse>>()
    val responseDeclineTripRequestServiceCall: LiveData<Response<RespondToTripRequestResponse>> = _responseDeclineTripRequestServiceCall

    private var _responseAcceptTripRequestServiceCall = MutableLiveData<Response<RespondToTripRequestResponse>>()
    val responseAcceptTripRequestServiceCall: LiveData<Response<RespondToTripRequestResponse>> = _responseAcceptTripRequestServiceCall

    private var _responseEndTripServiceCall = MutableLiveData<Response<RespondToTripRequestResponse>>()
    val responseEndTripServiceCall: LiveData<Response<RespondToTripRequestResponse>> = _responseEndTripServiceCall




    /** Update vehicle details service call */
    suspend fun updateVehicleDataServiceCall(status: VehicleStatus) {
        val request = UpdateVehicleRequest(
            cars_id = vehicleId,
            location = LocationData(lat = currentLat, lng = currentLng),
            state = status
        )

        val res = repository.updateVehicle(vehicleId!!, request)
        _responseUpdateVehicleServiceCall.postValue(res)

    }

    /** Get requested trip details service call */
    suspend fun getTripDetailsServiceCall() {
        val request = vehicleId!!

        val res = repository.getTripDetails(request)
        _responseGetTripDetailsServiceCall.postValue(res)

    }

    /** Decline trip request service call */
    suspend fun declineTripRequestServiceCall() {
        val request = tripId!!

        val res = repository.declineTripRequest(request)
        _responseDeclineTripRequestServiceCall.postValue(res)

    }


    /** Accept trip request service call */
    suspend fun acceptTripRequestServiceCall() {
        val request = tripId!!

        val res = repository.acceptTripRequest(request)
        _responseAcceptTripRequestServiceCall.postValue(res)

    }

    /** End trip service call */
    suspend fun endTripServiceCall() {
        val request = tripId!!

        val res = repository.endTrip(request)
        _responseEndTripServiceCall.postValue(res)
    }





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
    fun updateActiveDriverLocationData() {
        val map = mutableMapOf<String, String>()
        map["Lat"] = currentLat.toString()
        map["Lng"] = currentLng.toString()

        firebaseUtils.updateActiveDriverLocationData(
            driverId = driverId.toString(),
            map = map
        )

    }

    /** Update requested trip request status firebase service call */
    fun updateRideRequestStatus(status: String) {
        val map = mutableMapOf<String, String>()
        map["status"] = status

        firebaseUtils.updateRideRequestStatus(
            driverId = driverId.toString(),
            map = map
        )
    }




    //Retrieves data from SharedPreferences
    fun getDataFromSharedPreferences() {

        name = sharedPrefUtils.getData("NAME", "null")
        phone = sharedPrefUtils.getData("PHONE", "null")
        driverId= sharedPrefUtils.getData("DRIVER_ID", 0)

        vehicleName = sharedPrefUtils.getData("VEHICLE_NAME", "null")
        vehicleNo = sharedPrefUtils.getData("VEHICLE_NO", "null")
        vehicleId = sharedPrefUtils.getData("VEHICLE_ID", 0)

    }

    //Clears user stored data from SharedPreferences
    fun clearSharedPreData() {
        sharedPrefUtils.deleteData()

    }

}