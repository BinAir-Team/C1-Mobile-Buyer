package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.data.response.DataPromo
import binar.finalproject.binair.buyer.databinding.FragmentPromoBinding
import binar.finalproject.binair.buyer.ui.adapter.PagePromoAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel

class PromoFragment : Fragment() {
    private lateinit var binding : FragmentPromoBinding
    private lateinit var flightVM : FlightViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPromoBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPromo()
    }

    private fun getPromo() {
        flightVM.getAllPromo().observe(viewLifecycleOwner){
            if (it != null){
                setDatatoRecycleView(it)
            }
        }
    }

    private fun setDatatoRecycleView(data: List<DataPromo>) {
        val adapter = PagePromoAdapter(data)
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.recViewPromo.adapter = adapter
        binding.recViewPromo.layoutManager = layoutManager

        adapter.onClick = {
            val action = PromoFragmentDirections.actionPromoFragmentToDetailPromoFragment(it)
            findNavController().navigate(action)
        }
    }
}