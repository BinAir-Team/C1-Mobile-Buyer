package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.response.DataPromo
import binar.finalproject.binair.buyer.databinding.FragmentDetailPromoBinding
import com.bumptech.glide.Glide


class DetailPromoFragment : Fragment() {
    private lateinit var binding : FragmentDetailPromoBinding
    private lateinit var clickedpromo : DataPromo

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailPromoBinding.inflate(inflater, container, false)// Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromBundle()
        setDataToView()

    }

    private fun getDataFromBundle() {
        clickedpromo = arguments?.getParcelable("detailpromo")!!
    }

    private fun setDataToView() {
        binding.promo = clickedpromo
        context?.let {
            Glide.with(it)
                .load(clickedpromo.promoImage)
                .into(binding.ivPromo)
        }
    }
}