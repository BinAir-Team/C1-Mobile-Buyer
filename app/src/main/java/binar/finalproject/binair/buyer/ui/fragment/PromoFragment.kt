package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.response.DataPromo
import binar.finalproject.binair.buyer.databinding.FragmentPromoBinding
import binar.finalproject.binair.buyer.ui.adapter.PromoAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import binar.finalproject.binair.buyer.viewmodel.PromoViewModel

class PromoFragment : Fragment() {
    lateinit var binding: FragmentPromoBinding
    lateinit var FlightVM : PromoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPromoBinding.inflate(inflater, container,false)
        FlightVM = ViewModelProvider(requireActivity()).get(PromoViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getListPromo()

    }

    private fun getListPromo() {
        FlightVM.callgetPromo()
        FlightVM.allPromo.observe(viewLifecycleOwner){
            if (it != null){
                setDataToRecView(it)

            }
        }
    }

    private fun setDataToRecView(data: List<DataPromo>){
        val adapter: PromoAdapter = PromoAdapter(data)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.rvPromo.adapter = adapter
        binding.rvPromo.layoutManager = layoutManager

        adapter.onClick = {
            val action = PromoFragmentDirections.actionPromoFragmentToDetailPromoFragment()
            findNavController().navigate(action)

        }
    }

}