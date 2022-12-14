package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import binar.finalproject.binair.buyer.databinding.FragmentPaymentRoundedTripBinding

class PaymentRoundedTripFragment : Fragment() {
    private lateinit var binding : FragmentPaymentRoundedTripBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentRoundedTripBinding.inflate(inflater, container, false)
        return binding.root
    }

}