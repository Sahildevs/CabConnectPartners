package com.example.uberdrive.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uberdrive.data.model.*
import com.example.uberdrive.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    /** Driver details */
    var name: String? = null
    var phoneNumber: String? = null
    var license: String? = null
    var driverId: Int? =null

    /** Vehicle details */
    var modelName: String? = null
    var numberPlate: String? = null
    var vehicleId: Int? = null


    private val _responseAddDriverServiceCall = MutableLiveData<Response<AddDriverResponse>>()
    val responseAddDriverServiceCall : LiveData<Response<AddDriverResponse>> = _responseAddDriverServiceCall

    private val _responseAddVehicleServiceCall = MutableLiveData<Response<AddVehicleResponse>>()
    val responseAddVehicleServiceCall: LiveData<Response<AddVehicleResponse>> = _responseAddVehicleServiceCall



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


}