package com.binar.finalproject.view.akun

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.binar.finalproject.R
import com.binar.finalproject.databinding.FragmentUpdateProfileBinding
import com.binar.finalproject.local.DataStoreUser
import com.binar.finalproject.model.user.updateprofile.PutDataUpdateProfile
import com.binar.finalproject.utils.showCustomToast
import com.binar.finalproject.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProfileFragment : Fragment() {

    private lateinit var binding : FragmentUpdateProfileBinding
    private val userViewModel : UserViewModel by viewModels()
    private lateinit var dataStoreUser : DataStoreUser
    //contoh
    private var token : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        dataStoreUser = DataStoreUser(requireContext().applicationContext)

        binding.pbProfile.visibility = View.VISIBLE

        dataStoreUser.getToken.asLiveData().observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                token = it
                Log.d("TOKEN_USER", it.toString())
                setFillEditTextProfile()
            }
        }
       //nanti dikasih kondisi apakah toke null atau tidak

        binding.btnUpdateProfile.setOnClickListener {
            if(token.isNotEmpty()){
                updateProfileUser()
            }

        }
    }
    //set nilai pada fillEditText
    private fun setFillEditTextProfile() {
        userViewModel.getUserProfile(token)
        userViewModel.responseDataProfile.observe(viewLifecycleOwner){
            if(it != null){
                binding.etFullName.setText(it.data.nama)
                binding.etNoTelephone.setText(it.data.phone)
                binding.etEmail.setText(it.data.email)
                binding.pbProfile.visibility = View.GONE
            }
        }
    }

    private fun updateProfileUser() {
        val fullName = binding.etFullName.text.toString()
        val phoneNumber = binding.etNoTelephone.text.toString()

        if(fullName.isNotEmpty() && phoneNumber.isNotEmpty()){
            userViewModel.updateProfileUser(token,PutDataUpdateProfile(fullName, phoneNumber))

            binding.pbProfile.visibility = View.VISIBLE

            userViewModel.responseUpdateProfile.observe(viewLifecycleOwner){
                if(it != null){
                    Toast(requireContext()).showCustomToast(
                        it.message, requireActivity(), R.layout.toast_alert_green)
                    binding.etFullName.setText(it.data.nama)
                    binding.etNoTelephone.setText(it.data.phone)
                    binding.pbProfile.visibility = View.GONE
                }else{
                    Toast(requireContext()).showCustomToast(
                        "Update profile gagal!", requireActivity(), R.layout.toast_alert_red)
                    binding.pbProfile.visibility = View.GONE
                }
            }
        }else{
            Toast(requireContext()).showCustomToast(
                "Data tidak boleh kosong!", requireActivity(), R.layout.toast_alert_red)
        }

    }

}