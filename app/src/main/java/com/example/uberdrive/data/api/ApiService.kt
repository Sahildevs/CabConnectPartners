package com.example.uberdrive.data.api

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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    /********************** Login **********************/
    @GET("drivers")
    suspend fun checkDriverExists(@Query("phone_no") phone: Long): Response<DriverData>

    @GET("car_details")
    suspend fun getVehicleDetails(@Query("driver_id") driverId: Int): Response<AddVehicleResponse>


    /********************** Signup *********************/
    @POST("add_new_driver")
    suspend fun addDriver(@Body request: AddDriverRequest): Response<AddDriverResponse>

    @POST("cars")
    suspend fun addVehicle(@Body request: AddVehicleRequest): Response<AddVehicleResponse>


    /*********************** Landing ********************/
    @POST("cars/{cars_id}")
    suspend fun updateVehicle(@Path("cars_id") carId: Int, @Body request: UpdateVehicleRequest) : Response<UpdateVehicleResponse>

    @GET("get_trip_request")
    suspend fun getTripDetails(@Query("cars_id") carId: Int): Response<GetTripDetailsResponse>

    @POST("trip/{trip_id}")
    suspend fun declineTripRequest(@Path("trip_id") tripId: Int): Response<RespondToTripRequestResponse>

    @POST("start_trip/{trip_id}")
    suspend fun acceptTripRequest(@Path("trip_id") tripId: Int): Response<RespondToTripRequestResponse>

    @POST("end_trip/{trip_id}")
    suspend fun endTrip(@Path("trip_id") tripId: Int): Response<RespondToTripRequestResponse>














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