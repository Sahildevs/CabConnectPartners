package com.example.uberdrive.ui.onboarding.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.uberdrive.R
import com.example.uberdrive.databinding.FragmentOtpAuthBinding
import com.example.uberdrive.ui.onboarding.OnboardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtpAuthFragment : Fragment() {

    lateinit var binding: FragmentOtpAuthBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var OTP : String
    private lateinit var RESEND_TOKEN: PhoneAuthProvider.ForceResendingToken
    private lateinit var PHONE_NUMBER: String

    private val onboardViewModel: OnboardViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance() //Initialised firebase auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundleData()
        verifyOtp()
        serviceObserver()

    }

    /** Service Call */
    private fun checkIfDriverAlreadyExists(){
        lifecycleScope.launch {
            onboardViewModel.checkDriverExists()
        }
    }

    /** Service Call */
    private fun getVehicleDetails() {
        lifecycleScope.launch {
            onboardViewModel.getVehicleDetails()
        }
    }



    //Retrieve the data passed from the phoneAuth fragment
    private fun getBundleData() {

        OTP = arguments?.getString("OTP").toString()
        Log.d("OTP", OTP)
        //RESEND_TOKEN = arguments?.getParcelable("Resend_token")!!
        PHONE_NUMBER = arguments?.getString("PHONE_NUMBER").toString()

    }

    //Verifying the user entered otp
    private fun verifyOtp() {

        val otp1 = binding.etOtp1
        val otp2 = binding.etOtp2
        val otp3 = binding.etOtp3
        val opt4 = binding.etOtp4
        val otp5 = binding.etOtp5
        val otp6 = binding.etOtp6


        val btnVerify = binding.btnVerify

        btnVerify.setOnClickListener {

            //Getting the user entered OTP
            val enteredOtp = (otp1.text?.trim().toString() + otp2.text?.trim().toString() + otp3.text?.trim().toString() +
                    opt4.text?.trim().toString() + otp5.text?.trim().toString() + otp6.text?.trim().toString())

            if (enteredOtp.isNotEmpty()) {
                if (enteredOtp.length == 6) {
                    binding.loader2.visibility = View.VISIBLE

                    Log.d("OTP", OTP)
                    Log.d("OTP", enteredOtp)

                    //Verify the OTP
                    val credentials: PhoneAuthCredential = PhoneAuthProvider.getCredential(OTP, enteredOtp)

                    //Once credential are verified we pass the credentials to sign in the user
                    signInWithPhoneAuthCredential(credentials)

                }
                else {
                    Toast.makeText(requireActivity(), "Enter the full OTP", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(requireActivity(), "Enter the OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun serviceObserver() {

        onboardViewModel.responseCheckDriverExistsServiceCall.observe(viewLifecycleOwner, Observer { result->
            if (result.body() != null) {

                //Its a existing driver, skip the signup flow and login directly
                Toast.makeText(requireContext(), "User Already Exists", Toast.LENGTH_SHORT).show()
                binding.loader2.visibility = View.GONE

                //Save existing driver data to view model
                onboardViewModel.driverId = result.body()!!.id
                onboardViewModel.name = result.body()!!.name
                onboardViewModel.phoneNumber = result.body()!!.phone

                getVehicleDetails()

            }
            else {
                //Its a new user, continue with the signup flow
                Toast.makeText(requireContext(), "User Doesent Exists", Toast.LENGTH_SHORT).show()
                binding.loader2.visibility = View.GONE
                findNavController().navigate(R.id.action_otpAuthFragment_to_driverDetailsFragment)
            }
        })


        onboardViewModel.responseGetVehicleDetailsServiceCall.observe(viewLifecycleOwner, Observer { result->
            if (result.body() != null) {

                //Save vehicle details associated with existing driver in the view model
                onboardViewModel.vehicleId = result.body()!!.id
                onboardViewModel.modelName = result.body()!!.model
                onboardViewModel.numberPlate = result.body()!!.number

                storeDataToSharedPreference()
                navigateToMainLandingActivity()
            }
        })

    }

    //Add data to shared preference
    private fun storeDataToSharedPreference() {
        onboardViewModel.addDataToSharedPref()

    }

    private fun navigateToMainLandingActivity() {
        findNavController().navigate(R.id.action_otpAuthFragment_to_landingBaseActivity)

    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    //Store verified phone number in view model
                    onboardViewModel.phoneNumber = PHONE_NUMBER

                    checkIfDriverAlreadyExists()

                    //val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        binding.loader2.visibility = View.GONE
                        Toast.makeText(requireActivity(), "Invalid OTP", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", task.exception.toString())
                    }
                    // Update UI
                }
            }
    }


}