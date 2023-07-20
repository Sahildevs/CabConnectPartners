package com.example.uberdrive.ui.landing.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.uberdrive.R
import com.example.uberdrive.data.model.GetTripDetailsResponse
import com.example.uberdrive.data.model.VehicleStatus
import com.example.uberdrive.databinding.FragmentMapsBinding
import com.example.uberdrive.ui.landing.LandingBaseViewModel
import com.example.uberdrive.ui.landing.bottomsheets.RideRequestBottomSheet
import com.example.uberdrive.ui.landing.bottomsheets.RiderDetailsBottomSheet
import com.example.uberdrive.utils.FirebaseUtils
import com.example.uberdrive.utils.LocationUtils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class MapsFragment : Fragment(), RideRequestBottomSheet.Callback {

    lateinit var binding: FragmentMapsBinding

    private lateinit var locationUtils: LocationUtils
    private lateinit var mMap: GoogleMap

    private lateinit var firebaseUtils: FirebaseUtils
    private lateinit var listenerRegistration: ListenerRegistration

    private lateinit var rideRequestBottomSheet: RideRequestBottomSheet
    private lateinit var riderDetailsBottomSheet: RiderDetailsBottomSheet

    private val landingViewModel: LandingBaseViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //This class handles all location related operation
        locationUtils = LocationUtils(requireContext())

        firebaseUtils = FirebaseUtils()
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

        serviceObserver()
        onClick()
    }



    /** Service call */
    private fun updateVehicle(status: VehicleStatus) {
        lifecycleScope.launch {
            landingViewModel.updateVehicleServiceCall(status)
        }
    }

    /** Service call */
    private fun getTripDetails() {
        lifecycleScope.launch {
            landingViewModel.getTripDetailsServiceCall()
        }
    }

    /** Service call */
    private fun declineTripRequest() {
        lifecycleScope.launch {
            landingViewModel.declineTripRequestServiceCall()
        }
    }

    /** Service call */
    private fun acceptTripRequest() {
        lifecycleScope.launch {
            landingViewModel.acceptTripRequestServiceCall()
        }
    }

    private fun onClick() {

        binding.fabRiderDetails.setOnClickListener {
            showPassengerDetailsBottomSheet()
        }
    }


    private fun serviceObserver() {

        landingViewModel.responseGoLive.observe(viewLifecycleOwner, Observer { result ->
            if (result.isNotEmpty()) {
                when (result) {
                    "success" -> {
                        Toast.makeText(requireContext(), "You are live", Toast.LENGTH_SHORT).show()

                        updateVehicle(VehicleStatus.AVAILABLE)  //Update vehicle status in the db
                        startListeningRideRequests()            //Start listening to incoming ride requests
                    }

                    "failed" -> Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        landingViewModel.responseGoOffline.observe(viewLifecycleOwner, Observer { result ->
            if (result.isNotEmpty()) {
                when (result) {
                    "success" -> {
                        Toast.makeText(requireContext(), "You are offline", Toast.LENGTH_SHORT).show()

                        updateVehicle(VehicleStatus.BUSY)              //Update vehicle status in the db
                        stopListeningRideRequest()

                    }

                    "failed" -> Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        landingViewModel.responseUpdateVehicleServiceCall.observe(viewLifecycleOwner, Observer { result ->
            if (result.isSuccessful) {
                Toast.makeText(requireContext(), "Car Status ${result.body()?.state}", Toast.LENGTH_SHORT).show()

            }
        })

        landingViewModel.responseGetTripDetailsServiceCall.observe(viewLifecycleOwner, Observer { result ->
            if (result!= null) {

                landingViewModel.tripId = result.body()?.id
                landingViewModel.pickUpLat = result.body()?.pickUpLocation?.data?.lat
                landingViewModel.pickUpLng = result.body()?.pickUpLocation?.data?.lng
                landingViewModel.dropLat = result.body()?.dropLocation?.data?.lat
                landingViewModel.dropLng = result.body()?.dropLocation?.data?.lng
                landingViewModel.customerName = result.body()?.riderDetails?.name
                landingViewModel.customerPhone = result.body()?.riderDetails?.phone

                //Converts location points to readable address
                getPickupAddressFromLatLng(result)
                getDropAddressFromLatLng(result)

                showRideRequestBottomSheet()
            }
        })

        landingViewModel.responseDeclineTripRequestServiceCall.observe(viewLifecycleOwner, Observer { result ->
            if (result!= null) {
                Toast.makeText(requireContext(), "Request Denied", Toast.LENGTH_SHORT).show()
            }
        })

        landingViewModel.responseAcceptTripRequestServiceCall.observe(viewLifecycleOwner, Observer { result ->
            if (result != null) {
                Toast.makeText(requireContext(), result.body()?.state, Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun showRideRequestBottomSheet() {
        rideRequestBottomSheet = RideRequestBottomSheet(this)
        rideRequestBottomSheet.show(childFragmentManager, null)
        rideRequestBottomSheet.isCancelable = false
    }

    private fun showPassengerDetailsBottomSheet() {
        riderDetailsBottomSheet = RiderDetailsBottomSheet()
        riderDetailsBottomSheet.show(childFragmentManager, null)
        riderDetailsBottomSheet.isCancelable = false
    }

    //Retrieves pickup address from lat lng and store it in the view-model
    private fun getPickupAddressFromLatLng(result: Response<GetTripDetailsResponse>) {

        // Execute the geocoding operation on the coroutine
        lifecycleScope.launch {
            landingViewModel.pickUpAddress = locationUtils
                .getAddressFromLatLng(
                    lat = result.body()?.pickUpLocation?.data?.lat!!,
                    lng = result.body()?.pickUpLocation?.data?.lng!!
                )
        }

    }

    //Retrieves drop address from lat lng and store it in the view-model
    private fun getDropAddressFromLatLng(result: Response<GetTripDetailsResponse>) {

        // Execute the geocoding operation on the coroutine
        lifecycleScope.launch {
            landingViewModel.dropAddress = locationUtils
                .getAddressFromLatLng(
                    lat = result.body()?.dropLocation?.data?.lat!!,
                    lng = result.body()?.dropLocation?.data?.lng!!
                )
        }
    }



    //Listening to incoming ride request,
    private fun startListeningRideRequests() {
        listenerRegistration = firebaseUtils.getRideRequest(
            driverId = landingViewModel.driverId.toString(),
            onSuccess = {
                //Ride requested
                //Toast.makeText(this, "You got a ride request", Toast.LENGTH_SHORT).show()
                getTripDetails()

            },
            onFailure = {}
        )
    }


    //Stops listening to incoming ride requests
    private fun stopListeningRideRequest() {
        listenerRegistration.remove()
    }

    //Adds pickup location marker on map
    private fun addPickupPoint() {

        val iconBitmap = BitmapFactory.decodeResource(resources, R.drawable.pickup_marker)
        val customMarker = BitmapDescriptorFactory.fromBitmap(iconBitmap)

        val latLng = LatLng(landingViewModel.pickUpLat!!, landingViewModel.pickUpLng!!)
        mMap.addMarker(MarkerOptions().position(latLng).title("PickUp").icon(customMarker))
    }



    //Trip request accepted by the driver
    override fun onClickAcceptTripRequest() {
        landingViewModel.updateRideRequestStatus("ACCEPTED")
        acceptTripRequest()
        rideRequestBottomSheet.dismiss()
        binding.layoutActions.isVisible = true
        updateVehicle(VehicleStatus.BUSY)
        addPickupPoint()
    }

    //Trip request rejected by the driver
    override fun onClickRejectTripRequest() {
        landingViewModel.updateRideRequestStatus("REJECTED")
        declineTripRequest()
        rideRequestBottomSheet.dismiss()
        updateVehicle(VehicleStatus.AVAILABLE)
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                //Store updated location to view model
                landingViewModel.currentLat = location.latitude
                landingViewModel.currentLng = location.longitude

                //Update the vehicle location in firestore only if the driver is live
                if (landingViewModel.isLive) {
                    landingViewModel.updateActiveDriverData()
                }


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