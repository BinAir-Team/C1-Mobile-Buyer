package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.data.response.DataPromo
import binar.finalproject.binair.buyer.databinding.FragmentDetailPromoBinding
import binar.finalproject.binair.buyer.databinding.FragmentWishListDetailBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import com.github.ybq.android.spinkit.style.ThreeBounce
@SuppressLint("SetTextI18n")
class WishListDetailFragment : Fragment() {
    private lateinit var binding : FragmentWishListDetailBinding
    private lateinit var clickedWishlist : DataWishList
    private lateinit var flightVM : FlightViewModel
    private lateinit var sharedpref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedpref = requireActivity().getSharedPreferences(Constant.dataUser, 0)
        binding = FragmentWishListDetailBinding.inflate(inflater, container, false)// Inflate the layout for this fragment
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataToView()
        setListener()
    }

    private fun setDataToView() {
        clickedWishlist = arguments?.getParcelable("clickedWishlist")!!
        binding.ticket = clickedWishlist
    }

    private fun setListener(){
//        binding.toolbar.visibility = View.GONE

        binding.btnRemoveWishlist.setOnClickListener(){
            flightVM.deleteWishList(clickedWishlist.id)
            findNavController().navigate(R.id.action_wishListDetailFragment_to_wishlistFragment)
        }
    }
}