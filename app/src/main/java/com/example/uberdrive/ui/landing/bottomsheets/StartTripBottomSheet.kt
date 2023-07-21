package com.example.uberdrive.ui.landing.bottomsheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.example.uberdrive.databinding.BottomSheetStartTripBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class StartTripBottomSheet(private val callback: Callback) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetStartTripBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BottomSheetStartTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
    }

    private fun onClick() {

        binding.btnStartTrip.setOnClickListener {
            callback.startTrip()
        }

        binding.btnCall.setOnClickListener {
            callback.makePhoneCall()
        }
    }


    interface Callback{
        fun startTrip()
        fun makePhoneCall()
    }

}