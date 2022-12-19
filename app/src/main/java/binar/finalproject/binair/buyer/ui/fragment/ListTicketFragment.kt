package binar.finalproject.binair.buyer.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.data.model.SearchItem
import binar.finalproject.binair.buyer.data.response.TicketItem
import binar.finalproject.binair.buyer.databinding.FragmentListTicketBinding
import binar.finalproject.binair.buyer.ui.adapter.ListTicketAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import com.github.ybq.android.spinkit.style.ThreeBounce
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
        var res : List<TicketItem>? = null
        showLoading(true)
//        flightVM.callGetTicketBySearch(searchedTicket.cityFrom,searchedTicket.airportFrom,searchedTicket.cityTo,searchedTicket.airportTo,searchedTicket.dateGo,searchedTicket.type).observe(viewLifecycleOwner){
        flightVM.callGetTicketBySearch("","","","","","",).observe(viewLifecycleOwner){
            if (it != null) {
                res = it
                setDataToRecView(it)
                showLoading(false)
            }
        }
        Handler(Looper.myLooper()!!).postDelayed({
            if(res == null) {
                binding.tvTicketNotFound.visibility = View.VISIBLE
                showLoading(false)
            }
        },2500)
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
            binding.progressBar.setIndeterminateDrawable(ThreeBounce())
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}