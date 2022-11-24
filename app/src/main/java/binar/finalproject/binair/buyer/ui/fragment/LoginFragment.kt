package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.databinding.FragmentLoginBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.bottomNavContainer.visibility = View.GONE
    }

    private fun setListener() {
        binding.apply {
            tvRegister.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            btnSignin.setOnClickListener{
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }
}