package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.databinding.FragmentPaymentBinding
import binar.finalproject.binair.buyer.ui.adapter.PaymentMethodAdapter


class PaymentFragment : Fragment() {
    private lateinit var binding : FragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        binding.toolbar.tvTitlePage.text = "Pembayaran"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        initDataSpinner()
    }

    private fun setListener(){
        binding.ivPaymentProof.setOnClickListener {

        }
    }

    private fun initDataSpinner() {
        val arrName = arrayOf("Pilih pembayaran...","Bank BCA", "Bank Mandiri", "Bank BNI", "Bank BRI","Go-Pay","OVO","ShopeePay")
        val arrPhoto = arrayOf(null,R.drawable.logo_bca, R.drawable.logo_mandiri, R.drawable.logo_bni, R.drawable.logo_bri,R.drawable.logo_gopay,R.drawable.logo_ovo,R.drawable.logo_shopee_pay)
        binding.spPaymentMethod.adapter = PaymentMethodAdapter(requireContext(), R.layout.item_payment_method, arrName, arrPhoto)
    }

}