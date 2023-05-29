package com.example.uberdrive.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.uberdrive.R
import com.example.uberdrive.databinding.ActivityMainBinding

class OnboardBaseActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Retrieve nav controller from nav host fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container1) as NavHostFragment
        navController = navHostFragment.navController
    }
}