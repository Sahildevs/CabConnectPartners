package com.example.uberdrive.ui.landing.bottomsheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uberdrive.databinding.BottomSheetStartTripBinding


class StartTripBottomSheet : Fragment() {

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


}