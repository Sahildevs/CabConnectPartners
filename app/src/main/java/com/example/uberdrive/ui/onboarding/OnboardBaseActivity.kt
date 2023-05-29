package com.example.uberdrive.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.uberdrive.R
import com.example.uberdrive.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardBaseActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() //Initialised firebase auth

        window.statusBarColor = ContextCompat.getColor(this, R.color.black) //Set the status bar color

        //Retrieve nav controller from nav host fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container1) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser != null) {

        }
    }
}