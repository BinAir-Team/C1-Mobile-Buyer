package binar.finalproject.binair.buyer.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.databinding.FragmentChangePasswordBinding
import binar.finalproject.binair.buyer.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private lateinit var binding : FragmentChangePasswordBinding
    private lateinit var userVM : UserViewModel
    private lateinit var prefs : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        prefs = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    private fun setListener(){
        binding.apply {
            btnSimpan.setOnClickListener {
                val oldPassword = etOldPass.text.toString()
                val newPassword = etNewPass.text.toString()
                val confirmPassword = etConfirmPass.text.toString()
                updatePassword(oldPassword, newPassword, confirmPassword)
            }
            etOldPass.setOnFocusChangeListener { view, b ->
                if(!b && etOldPass.text.toString().isEmpty()){
                    etOldPass.error = "Password lama tidak boleh kosong"
                }
            }
            etNewPass.setOnFocusChangeListener { view, b ->
                if(!b && etNewPass.text.toString().isEmpty()){
                    etNewPass.error = "Password baru tidak boleh kosong"
                }
            }
            etConfirmPass.setOnFocusChangeListener { view, b ->
                if(!b && etConfirmPass.text.toString().isEmpty()){
                    etConfirmPass.error = "Konfirmasi Password tidak boleh kosong"
                }
            }
        }
    }

    private fun updatePassword(oldPass : String, newPass : String, confirmPass : String){
        val token = prefs.getString("token", null)
        if(validateInput() && isEqualPassConfPass(newPass, confirmPass)){
            if (token != null) {
                userVM.updatePassword(token,oldPass, newPass, confirmPass).observe(viewLifecycleOwner) {
                    if (it != null) {
                        Toast.makeText(requireContext(), "Update password berhasil", Toast.LENGTH_SHORT).show()
                        val edit = prefs.edit()
                        edit.clear()
                        edit.apply()
                        findNavController().navigate(R.id.action_global_homeFragment)
                    }
                }
            }
        }
    }

    private fun isEqualPassConfPass(newPass : String, confirmPass : String) : Boolean{
        return newPass == confirmPass
    }

    private fun validateInput() : Boolean{
        var res = true
        binding.apply {
            if (etOldPass.text.toString().isEmpty()){
                etOldPass.error = "Password tidak boleh kosong"
                res = false
            }
            if (etNewPass.text.toString().isEmpty()){
                etNewPass.error = "Password tidak boleh kosong"
                res = false
            }
            if (etConfirmPass.text.toString().isEmpty()){
                etConfirmPass.error = "Password tidak boleh kosong"
                res = false
            }
        }
        return res
    }
}