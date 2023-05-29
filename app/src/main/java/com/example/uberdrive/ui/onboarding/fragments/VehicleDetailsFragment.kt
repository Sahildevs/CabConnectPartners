package com.example.uberdrive.ui.onboarding.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uberdrive.R
import com.example.uberdrive.databinding.FragmentSplashBinding
import com.example.uberdrive.databinding.FragmentVehicleDetailsBinding


class VehicleDetailsFragment : Fragment() {

    lateinit var binding: FragmentVehicleDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVehicleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


}