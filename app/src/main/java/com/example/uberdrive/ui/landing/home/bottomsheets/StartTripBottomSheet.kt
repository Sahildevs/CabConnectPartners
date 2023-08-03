package com.example.uberdrive.ui.landing.home.bottomsheets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.airbnb.lottie.LottieAnimationView
import com.example.uberdrive.databinding.BottomSheetStartTripBinding
import com.example.uberdrive.ui.landing.LandingBaseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class StartTripBottomSheet(private val callback: Callback) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetStartTripBinding

    private val landingBaseViewModel: LandingBaseViewModel by activityViewModels()

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
            openDialer()
        }
    }

    //Open the phone dialer app passing the passengers phone number.
    private fun openDialer() {

        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:${landingBaseViewModel.customerPhone}")

        if (dialIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(dialIntent)
        }
        else {
            Toast.makeText(requireContext(), "No application found to complete the action", Toast.LENGTH_SHORT).show()
        }

    }


    interface Callback{
        fun startTrip()
    }

}