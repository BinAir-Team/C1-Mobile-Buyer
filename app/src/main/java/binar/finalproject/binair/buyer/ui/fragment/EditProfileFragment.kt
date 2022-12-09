package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.databinding.FragmentEditProfileBinding
import binar.finalproject.binair.buyer.viewmodel.UserViewModel

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    lateinit var userVM : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataUser()
    }

    private fun getDataUser(){
        val prefs = requireActivity().getSharedPreferences(Constant.dataUser, 0)
        val token = "Bearer " + prefs.getString("token", "")
        userVM.getUser(token).observe(viewLifecycleOwner) {
            if(it != null){
                val dt = it!!.data

            }
        }
    }
}