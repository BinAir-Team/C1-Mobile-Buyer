package binar.finalproject.binair.buyer.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.databinding.FragmentSpalshScreenBinding

class SpalshScreenFragment : Fragment() {
    lateinit var binding : FragmentSpalshScreenBinding
      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          binding = FragmentSpalshScreenBinding.inflate(inflater,container,false)
          return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.myLooper()!!).postDelayed({
//            if(username != null)
                findNavController().navigate(R.id.action_spalshScreenFragment_to_loginFragment)
//            else
//                findNavController().navigate(R.id.action_spalshScreenFragment_to_loginFragment)
        },2500)
    }


}