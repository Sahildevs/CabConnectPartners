package com.example.uberdrive.ui.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.uberdrive.R
import com.example.uberdrive.databinding.ActivityLandingBaseBinding
import com.example.uberdrive.ui.onboarding.OnboardBaseActivity
import com.example.uberdrive.ui.onboarding.bottomsheets.NetworkConnectionBottomSheet
import com.example.uberdrive.utils.FirebaseUtils
import com.example.uberdrive.utils.NetworkUtils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingBaseActivity : AppCompatActivity(), NetworkUtils.NetworkCallback {

    lateinit var binding: ActivityLandingBaseBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    private lateinit var firebaseUtils: FirebaseUtils
    private lateinit var networkUtils: NetworkUtils

    private val SHARED_PREF_FILE = "uberdrivedatastore"

    private var networkConnectionBottomSheet: NetworkConnectionBottomSheet? = null


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
    var lastBackPressedTime: Long = 0


    private val landingViewModel: LandingBaseViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkUtils = NetworkUtils(this)

        //Initialising the firebase utils class
        firebaseUtils = FirebaseUtils()


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

        retrieveUserDataFromSharedPref()

        onDrawerMenuClickListener()

    }

    override fun onResume() {
        super.onResume()

        //Register broadcast receiver to listen network state
        networkUtils.registerBroadcastReceiver(this, networkUtils)
    }

    override fun onPause() {
        super.onPause()

        //Unregister broadcast receiver
        networkUtils.unRegisterBroadcastReceiver(this, networkUtils)
    }


    //Retrieve the user data stored in shared preference
    private fun retrieveUserDataFromSharedPref() {
        landingViewModel.getDataFromSharedPreferences()

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

                    landingViewModel.clearSharedPreData()
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

    private fun showConnectionBottomSheet() {
        networkConnectionBottomSheet = NetworkConnectionBottomSheet()
        networkConnectionBottomSheet!!.show(supportFragmentManager, null)
        networkConnectionBottomSheet!!.isCancelable = false

    }


    override fun networkState(available: Boolean) {

        if (!available) {
            showConnectionBottomSheet()
        }
        else if (available && networkConnectionBottomSheet != null) {
            networkConnectionBottomSheet!!.dismiss()
        }

    }

    override fun onBackPressed() {

        val currentTime = System.currentTimeMillis()
        // If the time difference between the current and last back press is less than 2 seconds (2000 milliseconds),
        // consider it as a double press and exit the app.
        // If the time difference between the current and last back press is less than 2 seconds (2000 milliseconds),
        // consider it as a double press and exit the app.
        if (currentTime - lastBackPressedTime < 2000) {
            super.onBackPressed()
            // This will exit the app.

            this.finishAffinity()

        } else {
            // Inform the user to press back again to exit.
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }

        lastBackPressedTime = currentTime

    }


}