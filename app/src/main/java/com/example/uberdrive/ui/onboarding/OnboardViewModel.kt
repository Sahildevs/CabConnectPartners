package com.example.uberdrive.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uberdrive.data.model.*
import com.example.uberdrive.data.repository.MainRepository
import com.example.uberdrive.utils.SharedPrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(private val repository: MainRepository, private val sharedPrefUtils: SharedPrefUtils) : ViewModel() {

    /** Driver details */
    var name: String? = null
    var phoneNumber: String? = null
    var license: String? = null
    var driverId: Int? =null

    /** Vehicle details */
    var modelName: String? = null
    var numberPlate: String? = null
    var vehicleId: Int? = null


    private val _responseCheckDriverExistsServiceCall = MutableLiveData<Response<DriverData>>()
    val responseCheckDriverExistsServiceCall: LiveData<Response<DriverData>> = _responseCheckDriverExistsServiceCall

    private val _responseGetVehicleDetailsServiceCall = MutableLiveData<Response<AddVehicleResponse>>()
    val responseGetVehicleDetailsServiceCall: LiveData<Response<AddVehicleResponse>> = _responseGetVehicleDetailsServiceCall

    private val _responseAddDriverServiceCall = MutableLiveData<Response<AddDriverResponse>>()
    val responseAddDriverServiceCall : LiveData<Response<AddDriverResponse>> = _responseAddDriverServiceCall

    private val _responseAddVehicleServiceCall = MutableLiveData<Response<AddVehicleResponse>>()
    val responseAddVehicleServiceCall: LiveData<Response<AddVehicleResponse>> = _responseAddVehicleServiceCall



    /** Check driver exists service call*/
    suspend fun checkDriverExists() {
        val request = phoneNumber?.toLong()

        val res = repository.checkDriverExists(request!!)
        _responseCheckDriverExistsServiceCall.postValue(res)

    }

    suspend fun getVehicleDetails() {
        val request = driverId!!

        val res = repository.getVehicleDetails(request)
        _responseGetVehicleDetailsServiceCall.postValue(res)
    }

    /** Add driver service call*/
    suspend fun addDriverServiceCall() {
        val request = AddDriverRequest(
            name = name,
            phone = phoneNumber,
            license = license
        )

        val res = repository.addDriver(request)
        _responseAddDriverServiceCall.postValue(res)

    }

    /** Add vehicle service call*/
    suspend fun addVehicleServiceCall() {
        val request = AddVehicleRequest(
            model = modelName,
            number = numberPlate,
            drivers_id = driverId,
            state = VehicleStatus.BUSY
        )

        val res = repository.addVehicle(request)
        _responseAddVehicleServiceCall.postValue(res)

    }

    //Add data to shared preference
    fun addDataToSharedPref() {
        sharedPrefUtils.addData("NAME", name!!)
        sharedPrefUtils.addData("PHONE", phoneNumber!!)
        sharedPrefUtils.addData("DRIVER_ID", driverId!!)
        sharedPrefUtils.addData("VEHICLE_NAME", modelName!!)
        sharedPrefUtils.addData("VEHICLE_NO", numberPlate!!)
        sharedPrefUtils.addData("VEHICLE_ID", vehicleId!!)
    }


}