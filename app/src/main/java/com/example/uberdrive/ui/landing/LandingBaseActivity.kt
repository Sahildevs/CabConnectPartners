package com.example.uberdrive.ui.landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.uberdrive.R
import com.example.uberdrive.databinding.ActivityLandingBaseBinding
import com.example.uberdrive.ui.onboarding.OnboardBaseActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingBaseActivity : AppCompatActivity() {

    lateinit var binding: ActivityLandingBaseBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth


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

        firebaseServiceObserver()

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


    private fun firebaseServiceObserver() {

        landingViewModel.responseGoLive.observe(this, Observer {
            if (it.isNotEmpty()) {
                when (it) {
                    "success" -> Toast.makeText(this, "You are live", Toast.LENGTH_SHORT).show()

                    "failed" -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        landingViewModel.responseGoOffline.observe(this, Observer {
            if (it.isNotEmpty()) {
                when (it) {
                    "success" -> Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show()

                    "failed" -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })


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