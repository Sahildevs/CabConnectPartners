package com.example.uberdrive.ui.onboarding.bottomsheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uberdrive.R
import com.example.uberdrive.databinding.BottomSheetNetworkConnectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NetworkConnectionBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetNetworkConnectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BottomSheetNetworkConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }


}