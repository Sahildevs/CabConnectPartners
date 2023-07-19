package com.example.uberdrive.ui.landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.uberdrive.R
import com.example.uberdrive.data.model.GetTripDetailsResponse
import com.example.uberdrive.data.model.VehicleStatus
import com.example.uberdrive.databinding.ActivityLandingBaseBinding
import com.example.uberdrive.ui.landing.bottomsheets.RideRequestBottomSheet
import com.example.uberdrive.ui.onboarding.OnboardBaseActivity
import com.example.uberdrive.utils.FirebaseUtils
import com.example.uberdrive.utils.LocationUtils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class LandingBaseActivity : AppCompatActivity(), RideRequestBottomSheet.Callback {

    lateinit var binding: ActivityLandingBaseBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    private lateinit var firebaseUtils: FirebaseUtils
    private lateinit var listenerRegistration: ListenerRegistration
    private lateinit var locationUtils: LocationUtils

    private lateinit var rideRequestBottomSheet: RideRequestBottomSheet

    //ACCESSING VIEWS FROM LAYOUT
    private val actionbar : MaterialToolbar
        get() = binding.topAppBar

    private val drawerLayout : DrawerLayout
        get() = binding.drawerLayout

    private val navDrawer : NavigationView
        get() = binding.navDrawer

    private val switchButton: SwitchMaterial
        get() = binding.switchButton

    //back press timer
    var backPressedTime: Long = 0

    private val landingViewModel: LandingBaseViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialising the firebase utils class
        firebaseUtils = FirebaseUtils()

        //Initialising the location utils class
        locationUtils = LocationUtils(this)

        //Customized status bar
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        auth = FirebaseAuth.getInstance()

        //Retrieving NavController from the NavHost fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container2) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()


        //Defining top level destinations on which the action bar will be visible as well the hamburger
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.landingMapsFragment), drawerLayout
        )


        //SETTING THE TOOLBAR IN SYNC WITH THE NAVIGATION GRAPH
        setSupportActionBar(actionbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //On hamburger clicked open the navigation drawer
        actionbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        onSwitchButtonClicked()

        retrieveBundle()

        serviceObserver()

        onDrawerMenuClickListener()


    }

    //Retrieve the bundle passed from the onboarding activity
    private fun retrieveBundle() {

        val bundle = intent.extras
        if (bundle != null) {
            landingViewModel.name = bundle.getString("NAME")
            landingViewModel.phone = bundle.getString("PHONE")
            landingViewModel.driverId = bundle.getInt("DRIVER_ID")
            landingViewModel.vehicleName = bundle.getString("VEHICLE_NAME")
            landingViewModel.vehicleNo = bundle.getString("VEHICLE_NO")
            landingViewModel.vehicleId = bundle.getInt("VEHICLE_ID")
        }

    }

    //Navigation drawer menu item click listener
    private fun onDrawerMenuClickListener() {

        navDrawer.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.profile -> {
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                    //drawerLayout.closeDrawer(Gravity.LEFT)
                }


                R.id.logout -> {
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    startActivity(Intent(this, OnboardBaseActivity::class.java))
                    finish()
                }

            }
            true
        }

    }

    //Handle the vehicle discoverable status
    private fun onSwitchButtonClicked() {

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switched ON
                landingViewModel.isLive = true
                goLive()

            } else {
                // Switched OFF
                goOffline()
            }
        }

    }


    /** Firebase service call */
    private fun goLive() {
        landingViewModel.goLive()
    }

    /** Firebase service call */
    private fun goOffline() {
        landingViewModel.goOffline()
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



    private fun serviceObserver() {

        landingViewModel.responseGoLive.observe(this, Observer { result ->
            if (result.isNotEmpty()) {
                when (result) {
                    "success" -> {
                        Toast.makeText(this, "You are live", Toast.LENGTH_SHORT).show()

                        updateVehicle(VehicleStatus.AVAILABLE)  //Update vehicle status in the db
                        startListeningRideRequests()            //Start listening to incoming ride requests
                                 }

                    "failed" -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        landingViewModel.responseGoOffline.observe(this, Observer { result ->
            if (result.isNotEmpty()) {
                when (result) {
                    "success" -> {
                        Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show()

                        updateVehicle(VehicleStatus.BUSY)              //Update vehicle status in the db
                        stopListeningRideRequest()

                    }

                    "failed" -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        landingViewModel.responseUpdateVehicleServiceCall.observe(this, Observer { result ->
            if (result.isSuccessful) {
                Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()

            }
        })

        landingViewModel.responseGetTripDetailsServiceCall.observe(this, Observer { result ->
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

        landingViewModel.responseDeclineTripRequestServiceCall.observe(this, Observer { result ->
            if (result!= null) {
                Toast.makeText(this, "Request Denied", Toast.LENGTH_SHORT).show()
            }
        })

        landingViewModel.responseAcceptTripRequestServiceCall.observe(this, Observer { result ->
            if (result != null) {
                Toast.makeText(this, result.body()?.state, Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun showRideRequestBottomSheet() {
        rideRequestBottomSheet = RideRequestBottomSheet(this)
        rideRequestBottomSheet.show(supportFragmentManager, null)
        rideRequestBottomSheet.isCancelable = false
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



    //Trip request accepted by the driver
    override fun onClickAcceptTripRequest() {
        landingViewModel.updateRideRequestStatus("ACCEPTED")
        acceptTripRequest()
        rideRequestBottomSheet.dismiss()
    }

    //Trip request rejected by the driver
    override fun onClickRejectTripRequest() {
        landingViewModel.updateRideRequestStatus("REJECTED")
        declineTripRequest()
        rideRequestBottomSheet.dismiss()
    }


    override fun onBackPressed() {
        super.onBackPressed()

        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity()
        } else {
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()

    }


}