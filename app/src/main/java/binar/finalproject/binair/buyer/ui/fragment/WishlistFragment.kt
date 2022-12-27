package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.databinding.FragmentWishlistBinding
import binar.finalproject.binair.buyer.ui.adapter.WishListAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment() {
    private lateinit var binding: FragmentWishlistBinding
    private lateinit var flightVM : FlightViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getAllWishlist()
    }

    @SuppressLint("SetTextI18n")
    private fun getAllWishlist(){
        val prefs = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE)
        val idUser = prefs.getString("idUser",null)
        if (idUser != null) {
            flightVM.getAllWishlist(idUser).observe(viewLifecycleOwner){
                if (it != null) {
                    setDataToRecView(it)
                }else{
                    binding.tvAlertKosong.visibility = View.VISIBLE
                }
            }
        }else{
            binding.tvAlertKosong.text = "Silahkan login terlebih dahulu untuk menggunakan fitur ini"
            binding.tvAlertKosong.visibility = View.VISIBLE
        }
    }

    private fun setDataToRecView(data: List<DataWishList>) {
        val adapter = WishListAdapter()
        binding.apply {
            adapter.setData(data)
            RvWishlist.adapter = adapter
            RvWishlist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        adapter.onClick = {
            val action = WishlistFragmentDirections.actionWishlistFragment2ToWishListDetailFragment(it)
            findNavController().navigate(action)
        }
    }


}