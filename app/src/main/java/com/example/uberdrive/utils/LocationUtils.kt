package com.example.uberdrive.utils


import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.Locale

class LocationUtils(private val context: Context) {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationListener: LocationListenerCallback? = null
    private var locationReceiver: LocationReceiverCallback? = null



    //Checks whether the required permissions are granted or not and returns a boolean value
    // indicating whether the permissions are granted.
    fun checkLocationPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    }


    //Starts receiving location updates on a regular interval
    fun startLocationUpdates(locationListener: LocationListenerCallback) {
        this.locationListener = locationListener

        if (!checkLocationPermission()){
            //Handle permission not granted
            return
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)


        locationRequest = LocationRequest()
        locationRequest.interval = 10000 //10sec
        locationRequest.fastestInterval = 5000  //5sec
        locationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    locationListener.onLocationChanged(it)
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

    }


    //Stops receiving location updates
    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    //Retrieves the last known location of the device
    fun getLastKnownLocation(locationReceiver: LocationReceiverCallback) {
        this.locationReceiver = locationReceiver

        if (!checkLocationPermission()) {
            //Handle permission not granted
            locationReceiver.onLocationReceived(null) //indicating that the last known location cannot be retrieved due to lack of permissions.
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            //Handle last known location
            locationReceiver.onLocationReceived(location)
        }

    }


    //Converts latitude and longitude coordinates into a human-readable address using the Geocoding API
    fun getAddressFromLatLng(lat: Double, lng: Double): String {

        val geocoder = Geocoder(context, Locale.getDefault())

        //The getFromLocation() method of the Geocoder class is called, passing the latitude, longitude,
        // and a maximum result value of 1. This retrieves a list of addresses associated with the provided coordinates.
        val addresses = geocoder.getFromLocation(lat, lng, 1)

        if (addresses!!.isNotEmpty()) {

            //Retrieves the first address from the list
            val address = addresses[0]

            //StringBuilder is created to concatenate the individual address lines.
            //val sb = StringBuilder()

            // Extract individual address lines
//            for (i in 0..address.) {
//                sb.append(address.getAddressLine(i)).append(" ")
//            }

            return address.subLocality

        }

        return "Address not found"

    }


    interface LocationListenerCallback {
        fun onLocationChanged(location: Location)
    }

    interface LocationReceiverCallback {
        fun onLocationReceived(location: Location?)
    }



}