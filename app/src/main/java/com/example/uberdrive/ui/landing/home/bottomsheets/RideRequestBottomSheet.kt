package com.example.uberdrive.ui.landing.home.bottomsheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.uberdrive.R
import com.example.uberdrive.databinding.BottomSheetRideRequestBinding
import com.example.uberdrive.ui.landing.LandingBaseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RideRequestBottomSheet(private val callback: Callback) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetRideRequestBinding

    private val landingBaseViewModel: LandingBaseViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BottomSheetRideRequestBinding.inflate(inflater, container, false)
        binding.apply {
            viewmodel = landingBaseViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
    }

    private fun onClick() {

        binding.btnAccept.setOnClickListener {
            callback.onClickAcceptTripRequest()
        }

        binding.btnDecline.setOnClickListener {
            callback.onClickRejectTripRequest()
        }
    }

    interface Callback {
        fun onClickAcceptTripRequest()
        fun onClickRejectTripRequest()
    }

}