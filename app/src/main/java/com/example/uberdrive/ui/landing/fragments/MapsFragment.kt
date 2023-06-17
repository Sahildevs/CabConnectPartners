package com.example.uberdrive.ui.landing.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.uberdrive.R
import com.example.uberdrive.databinding.FragmentMapsBinding
import com.example.uberdrive.utils.LocationUtils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    lateinit var binding: FragmentMapsBinding

    private lateinit var locationUtils: LocationUtils
    private lateinit var mMap: GoogleMap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //This class handles all location related operation
        locationUtils = LocationUtils(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Here we find the map fragment and inflate it, once inflated it calls a callback
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }



















    // Once the map is ready this callback will be triggered, and here we call a method to check for the permissions
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap
        checkForLocationPermission()
    }


    private fun checkForLocationPermission() {

        if (locationUtils.checkLocationPermission()) {
            getUpdatedLocation()
        }
        else {
            requestLocationPermission()
        }

    }


    //Request for location permissions
    private fun requestLocationPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        locationPermissionLauncher.launch(permissions)
    }


    //Start receiving location updates
    @SuppressLint("MissingPermission")
    private fun getUpdatedLocation() {

        mMap.isMyLocationEnabled = true

        locationUtils.startLocationUpdates(object : LocationUtils.LocationListenerCallback{
            override fun onLocationChanged(location: Location) {
                //Handle location updates

                val latLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
            }
        })

    }


    //Stop receiving location updates
    private fun stopLocationUpdates() {
        locationUtils.stopLocationUpdates()
    }










    //Checks permission grant result
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.all { it.value }

            if (allPermissionsGranted) {
                // Start location updates or perform other actions requiring location permission
                getUpdatedLocation()
            } else {
                // Handle the case where some or all permissions were not granted
                // You can show a message or take appropriate action here
            }
        }



}