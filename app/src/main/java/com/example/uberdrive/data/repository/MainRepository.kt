package com.example.uberdrive.data.repository

import com.example.uberdrive.data.api.ApiService
import com.example.uberdrive.data.model.AddDriverRequest
import com.example.uberdrive.data.model.AddDriverResponse
import com.example.uberdrive.data.model.AddVehicleRequest
import com.example.uberdrive.data.model.AddVehicleResponse
import com.example.uberdrive.data.model.UpdateVehicleRequest
import com.example.uberdrive.data.model.UpdateVehicleResponse
import com.example.uberdrive.data.model.VehicleStatus
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun addDriver(request: AddDriverRequest): Response<AddDriverResponse> {
        return apiService.addDriver(request = request)

    }

    suspend fun addVehicle(request: AddVehicleRequest): Response<AddVehicleResponse> {
        return apiService.addVehicle(request = request)
    }

    suspend fun updateVehicle(carsId: Int, request: UpdateVehicleRequest): Response<UpdateVehicleResponse> {
        return apiService.updateVehicle(carId = carsId, request = request)
    }
}