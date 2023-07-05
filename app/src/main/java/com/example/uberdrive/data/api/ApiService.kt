package com.example.uberdrive.data.api

import com.example.uberdrive.data.model.AddDriverRequest
import com.example.uberdrive.data.model.AddDriverResponse
import com.example.uberdrive.data.model.AddVehicleRequest
import com.example.uberdrive.data.model.AddVehicleResponse
import com.example.uberdrive.data.model.UpdateVehicleRequest
import com.example.uberdrive.data.model.UpdateVehicleResponse
import com.example.uberdrive.data.model.VehicleStatus
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {


    @POST("add_new_driver")
    suspend fun addDriver(@Body request: AddDriverRequest): Response<AddDriverResponse>

    @POST("cars")
    suspend fun addVehicle(@Body request: AddVehicleRequest): Response<AddVehicleResponse>

    @POST("cars/{cars_id}")
    suspend fun updateVehicle(@Path("cars_id") carId: Int, @Body request: UpdateVehicleRequest) : Response<UpdateVehicleResponse>














    companion object {
        private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:KkRqjwUP/"

        var apiService: ApiService? = null

        fun getInstance(): ApiService {
            if (apiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                apiService = retrofit.create(ApiService::class.java)
            }
            return apiService!!
        }
    }

}