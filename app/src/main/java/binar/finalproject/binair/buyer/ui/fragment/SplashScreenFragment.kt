package binar.finalproject.binair.buyer.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant.dataUser
import binar.finalproject.binair.buyer.data.Constant.initApp
import binar.finalproject.binair.buyer.databinding.FragmentSplashScreenBinding
import binar.finalproject.binair.buyer.viewmodel.UserViewModel

class SplashScreenFragment : Fragment() {
    private lateinit var binding : FragmentSplashScreenBinding
    private lateinit var sharedPref : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater,container,false)
        sharedPref = requireActivity().getSharedPreferences(initApp, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        val popIn = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.pop_up)
        binding.mainlogo.startAnimation(popIn)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val checkToken = checkToken()
        Handler(Looper.myLooper()!!).postDelayed({
            if(sharedPref.getBoolean("firstRun",true)) {
                editor.putBoolean("firstRun", false)
                editor.apply()
                findNavController().navigate(R.id.action_spalshScreenFragment_to_carouselFragment2)
            }else{
                findNavController().navigate(R.id.action_spalshScreenFragment_to_homeFragment)
            }
        },2500)
    }

    private fun checkToken(){
        val prefs = requireActivity().getSharedPreferences(dataUser, 0)
        val edit = prefs.edit()
        val token = prefs.getString("token", null)
        val userVM = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        if(token != null){
            userVM.getUser("Bearer $token").observe(viewLifecycleOwner) {
                if(it == null){
                    edit.putBoolean("isValidToken",false)
                    edit.putString("token", null)
                }
            }
        }else{
            edit.putBoolean("isValidToken",false)
            edit.putString("token", null)
        }
        edit.apply()
    }
}