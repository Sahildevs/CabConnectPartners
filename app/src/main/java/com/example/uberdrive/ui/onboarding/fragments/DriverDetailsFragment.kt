package com.example.uberdrive.ui.onboarding.fragments

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
import com.example.uberdrive.databinding.FragmentDriverDetailsBinding
import com.example.uberdrive.databinding.FragmentSplashBinding
import com.example.uberdrive.ui.onboarding.OnboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DriverDetailsFragment : Fragment() {

    lateinit var binding: FragmentDriverDetailsBinding

    private val onboardViewModel: OnboardViewModel by activityViewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        serviceObserver()

    }



    /** Service Call */
    private fun addDriver() {
        lifecycleScope.launch {
            onboardViewModel.addDriverServiceCall()
        }
    }


    private fun onClick() {

        binding.btnNext.setOnClickListener {
            val areInputsFilled = checkInputFields(binding.etName, binding.etPhone, binding.etLicense)

            if (areInputsFilled) {
                getFieldValues()
                addDriver()
            }
            else {
                Toast.makeText(requireContext(), "Please provide all details", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Checks if all the input fields are filled
    private fun checkInputFields(vararg editTexts: EditText): Boolean {
        for (editText in editTexts) {
            if (editText.text.isEmpty()) {
                return false
            }
        }
        return true
    }

    //Store all user entered inputs in view model
    private fun getFieldValues() {
        onboardViewModel.name = binding.etName.text.trim().toString()
        onboardViewModel.phoneNumber = binding.etPhone.text.trim().toString()
        onboardViewModel.license = binding.etLicense.text.trim().toString()
    }



    private fun serviceObserver() {

        onboardViewModel.responseAddDriverServiceCall.observe(viewLifecycleOwner) { result ->
            if (result != null && result.isSuccessful) {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                //Store data to view model
                onboardViewModel.driverId = result.body()?.driver?.id
                onboardViewModel.name = result.body()?.driver?.name
                onboardViewModel.phoneNumber = result.body()?.driver?.phone

                navigateToVehicleDetails()

            }
            else {
                Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToVehicleDetails() {
        findNavController().navigate(R.id.action_driverDetailsFragment_to_vehicleDetailsFragment)

    }

}