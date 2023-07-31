package com.example.uberdrive.data.repository

import com.example.uberdrive.data.api.ApiService
import com.example.uberdrive.data.model.AddDriverRequest
import com.example.uberdrive.data.model.AddDriverResponse
import com.example.uberdrive.data.model.AddVehicleRequest
import com.example.uberdrive.data.model.AddVehicleResponse
import com.example.uberdrive.data.model.DriverData
import com.example.uberdrive.data.model.RespondToTripRequestResponse
import com.example.uberdrive.data.model.GetTripDetailsResponse
import com.example.uberdrive.data.model.UpdateVehicleRequest
import com.example.uberdrive.data.model.UpdateVehicleResponse
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService) {


    suspend fun checkDriverExists(request: Long): Response<DriverData> {
        return apiService.checkDriverExists(request)
    }

    suspend fun getVehicleDetails(request: Int): Response<AddVehicleResponse> {
        return apiService.getVehicleDetails(request)
    }

    suspend fun addDriver(request: AddDriverRequest): Response<AddDriverResponse> {
        return apiService.addDriver(request = request)
    }

    suspend fun addVehicle(request: AddVehicleRequest): Response<AddVehicleResponse> {
        return apiService.addVehicle(request = request)
    }

    suspend fun updateVehicle(carsId: Int, request: UpdateVehicleRequest): Response<UpdateVehicleResponse> {
        return apiService.updateVehicle(carId = carsId, request = request)
    }

    suspend fun getTripDetails(request: Int): Response<GetTripDetailsResponse> {
        return apiService.getTripDetails(request)
    }

    suspend fun declineTripRequest(request: Int): Response<RespondToTripRequestResponse> {
        return apiService.declineTripRequest(request)
    }

    suspend fun acceptTripRequest(request: Int): Response<RespondToTripRequestResponse> {
        return apiService.acceptTripRequest(request)
    }

    suspend fun endTrip(request: Int): Response<RespondToTripRequestResponse> {
        return apiService.endTrip(request)
    }
}