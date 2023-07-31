package com.example.uberdrive.ui.onboarding.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.uberdrive.R
import com.example.uberdrive.databinding.FragmentVehicleDetailsBinding
import com.example.uberdrive.ui.onboarding.OnboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehicleDetailsFragment : Fragment() {

    lateinit var binding: FragmentVehicleDetailsBinding

    private val onboardViewModel: OnboardViewModel by activityViewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        serviceObserver()

    }

    /** Network call */
    private fun addVehicle() {
        lifecycleScope.launch {
            onboardViewModel.addVehicleServiceCall()
        }
    }

    private fun onClick() {

        binding.btnLogin.setOnClickListener {
            val areInputFilled = checkInputFields(binding.etModelName, binding.etNumberPlate)

            if (areInputFilled) {
                getFieldValues()
                addVehicle()
            }
            else {
                Toast.makeText(requireContext(), "Please provide all details", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //Check if all input fields are filled
    private fun checkInputFields(vararg editTexts: EditText): Boolean {
        for (editText in  editTexts) {
            if (editText.text.isEmpty()) {
                return false
            }
        }
        return true
    }


    //Store user entered inputs in viewmodel
    private fun getFieldValues() {
        onboardViewModel.modelName = binding.etModelName.text.trim().toString()
        onboardViewModel.numberPlate = binding.etNumberPlate.text.trim().toString()
    }


    private fun serviceObserver() {

        onboardViewModel.responseAddVehicleServiceCall.observe(viewLifecycleOwner) { result->
            if (result != null && result.isSuccessful) {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                //Store data to view model
                onboardViewModel.vehicleId = result.body()?.id

                storeDataToSharedPreference()
                navigateToLandingMainActivity()

            }
            else {
                Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Store all the driver and car details to shared preferences
    private fun storeDataToSharedPreference() {
        onboardViewModel.addDataToSharedPref()

    }

    //Navigates to the landing activity with
    private fun navigateToLandingMainActivity() {
        findNavController().navigate(R.id.action_vehicleDetailsFragment_to_landingBaseActivity)

    }


}