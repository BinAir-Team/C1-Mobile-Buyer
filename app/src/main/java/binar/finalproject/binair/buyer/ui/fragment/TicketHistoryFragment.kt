package binar.finalproject.binair.buyer.ui.fragment

import android.content.Context
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
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.response.TransItem
import binar.finalproject.binair.buyer.databinding.FragmentTicketHistoryBinding
import binar.finalproject.binair.buyer.ui.adapter.TicketHistoryAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel

@RequiresApi(Build.VERSION_CODES.O)
class TicketHistoryFragment : Fragment() {
    private lateinit var binding : FragmentTicketHistoryBinding
    private lateinit var flightVM : FlightViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicketHistoryBinding.inflate(layoutInflater)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getTrans()
    }

    private fun getTrans(){
        showLoading(true)
        binding.tvNotLogin.visibility = View.GONE
        val token = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE).getString("token", null)
        if (token != null) {
            flightVM.getUserTrans("Bearer $token").observe(viewLifecycleOwner){
                if (it != null) {
                    showLoading(false)
                    setDataToRV(it)
                }else{
                    showLoading(false)
                }
            }
        }else{
            showLoading(false)
            binding.tvNotLogin.visibility = View.VISIBLE
        }
    }

    private fun setDataToRV(data : List<TransItem>){
        val adapter = TicketHistoryAdapter(data)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recViewHistory.adapter = adapter
        binding.recViewHistory.layoutManager = layoutManager
        adapter.onClick = {
            for(trav in it.traveler){
                if (trav.noKtp == null){
                    trav.noKtp = ""
                    trav.idCard = ""
                }
            }
            if(it.ticket.dateEnd == null){
                it.ticket.dateEnd = ""
            }
            if(it.user.gender == null){
                it.user.gender = ""
            }
            if(it.status.contains("PENDING PAYMENT")){
                val act = TicketHistoryFragmentDirections.actionTicketHistoryFragment2ToPaymentFragment(it)
                findNavController().navigate(act)
            }else{
                val act = TicketHistoryFragmentDirections.actionTicketHistoryFragment2ToEticketFragment(it)
                findNavController().navigate(act)
            }
        }
    }

    private fun showLoading(status : Boolean){
        if(status) {
            binding.shimmerHistory.shimmerHistory.visibility = View.VISIBLE
            binding.shimmerHistory.shimmerHistory.startShimmerAnimation()
        }else{
            binding.shimmerHistory.shimmerHistory.visibility = View.GONE
            binding.shimmerHistory.shimmerHistory.stopShimmerAnimation()
        }
    }
}