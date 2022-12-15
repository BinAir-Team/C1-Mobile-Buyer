package binar.finalproject.binair.buyer.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
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
        setListener()
        getTrans()
    }

    private fun setListener(){
        binding.timefilter.setOnClickListener(){
                v: View ->
            showMenu(v, R.menu.filter_popupmenu)
        }
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

        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
                menuItem: MenuItem -> true
            // Respond to menu item click.
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
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