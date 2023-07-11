package com.example.uberdrive.ui.landing.bottomsheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uberdrive.R
import com.example.uberdrive.databinding.BottomSheetEndTripBinding
import com.example.uberdrive.databinding.BottomSheetRideRequestBinding


class EndTripBottomSheet : Fragment() {

    lateinit var binding: BottomSheetEndTripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BottomSheetEndTripBinding.inflate(inflater, container, false)
        return binding.root
    }


}