package com.example.uberdrive.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uberdrive.data.model.AddDriverRequest
import com.example.uberdrive.data.model.AddDriverResponse
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


    private val _responseAddDriverServiceCall = MutableLiveData<Response<AddDriverResponse>>()
    val responseAddDriverServiceCall : LiveData<Response<AddDriverResponse>> = _responseAddDriverServiceCall



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


}