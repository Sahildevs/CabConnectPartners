package com.example.uberdrive.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.uberdrive.R
import com.example.uberdrive.databinding.ActivityMainBinding
import com.example.uberdrive.ui.onboarding.bottomsheets.NetworkConnectionBottomSheet
import com.example.uberdrive.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardBaseActivity : AppCompatActivity(), NetworkUtils.NetworkCallback {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    private lateinit var auth: FirebaseAuth
    private lateinit var networkUtils: NetworkUtils

    private  var networkConnectionBottomSheet: NetworkConnectionBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkUtils = NetworkUtils(this)

        auth = FirebaseAuth.getInstance() //Initialised firebase auth


        window.statusBarColor = ContextCompat.getColor(this, R.color.black) //Set the status bar color

        //Retrieve nav controller from nav host fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container1) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onStart() {
        super.onStart()


    }

    override fun onResume() {
        super.onResume()

        //Register the broadcast receiver to listen for network state
        networkUtils.registerBroadcastReceiver(this, networkUtils)
    }

    override fun onPause() {
        super.onPause()

        //Unregister broadcast receiver
        networkUtils.unRegisterBroadcastReceiver(this, networkUtils)
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
        else if (available && networkConnectionBottomSheet!= null) {
            networkConnectionBottomSheet!!.dismiss()
        }



    }
}