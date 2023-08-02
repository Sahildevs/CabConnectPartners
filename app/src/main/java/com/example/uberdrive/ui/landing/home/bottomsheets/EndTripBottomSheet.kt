package com.example.uberdrive.ui.landing.home.bottomsheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.example.uberdrive.R
import com.example.uberdrive.databinding.BottomSheetEndTripBinding
import com.example.uberdrive.databinding.BottomSheetRideRequestBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EndTripBottomSheet(private val callback: Callback) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetEndTripBinding
    private lateinit var animationView: LottieAnimationView

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAnim()
        onClick()
    }

    private fun onClick() {
        binding.btnEndTrip.setOnClickListener {
            callback.endTrip()
        }
    }

    private fun showAnim() {
        animationView = binding.animAccepted
        animationView.setAnimation("flag.json")
        animationView.playAnimation()
    }

    interface Callback{
        fun endTrip()
    }

}