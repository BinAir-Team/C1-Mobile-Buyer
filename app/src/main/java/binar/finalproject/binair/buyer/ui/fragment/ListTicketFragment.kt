package binar.finalproject.binair.buyer.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.model.SearchItem
import binar.finalproject.binair.buyer.data.response.TicketItem
import binar.finalproject.binair.buyer.databinding.FragmentListTicketBinding
import binar.finalproject.binair.buyer.ui.adapter.ListTicketAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class ListTicketFragment : Fragment() {
    private lateinit var binding: FragmentListTicketBinding
    private lateinit var flightVM : FlightViewModel
    private lateinit var searchedTicket : SearchItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListTicketBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSearchedTicket()
        getListTicket()
    }

    private fun getSearchedTicket() {
        searchedTicket = arguments?.getParcelable("searchedTicket")!!
    }

    private fun getListTicket() {
        showLoading(true)
//        flightVM.callGetAllTicket().observe(viewLifecycleOwner) {
//            if (it != null) {
//                setDataToRecView(it)
//                showLoading(false)
//            }
//        }
        flightVM.callGetTicketBySearch(searchedTicket.cityFrom,searchedTicket.airportFrom,searchedTicket.cityTo,searchedTicket.airportTo,searchedTicket.dateGo,searchedTicket.type).observe(viewLifecycleOwner){
            if (it != null) {
                setDataToRecView(it)
                showLoading(false)
                binding.tvTicketNotFound.visibility = View.GONE
            }else{
                showLoading(false)
                binding.tvTicketNotFound.visibility = View.VISIBLE
            }
        }
    }

    private fun setDataToRecView(data: List<TicketItem>) {
        val adapter = ListTicketAdapter(data)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvListTicket.adapter = adapter
        binding.rvListTicket.layoutManager = layoutManager

        adapter.onClick = {
            flightVM.clearChosenTicket()
            flightVM.setChosenTicket(it)
            findNavController().navigate(R.id.action_listTicketFragment_to_ticketDetailsFragment)
        }
    }

    private fun showLoading(condition : Boolean) {
        if (condition) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}