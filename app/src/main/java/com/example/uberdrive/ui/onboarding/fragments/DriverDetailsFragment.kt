package com.example.uberdrive.ui.onboarding.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uberdrive.R
import com.example.uberdrive.databinding.FragmentDriverDetailsBinding
import com.example.uberdrive.databinding.FragmentSplashBinding


class DriverDetailsFragment : Fragment() {

    lateinit var binding: FragmentDriverDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDriverDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


}