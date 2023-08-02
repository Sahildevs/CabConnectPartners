package com.example.uberdrive.ui.landing.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.uberdrive.R
import com.example.uberdrive.databinding.FragmentUserProfileBinding
import com.example.uberdrive.ui.landing.LandingBaseViewModel


class UserProfileFragment : Fragment() {

    lateinit var binding: FragmentUserProfileBinding

    private val landingBaseViewModel: LandingBaseViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        binding.apply {
            viewmodel = landingBaseViewModel
        }
        return binding.root

    }


}