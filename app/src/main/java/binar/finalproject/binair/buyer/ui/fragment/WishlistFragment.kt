package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.timefilter.setOnClickListener() { v: View ->
            showMenu(v, R.menu.filter_popupmenu)
        }

        getAllNote()
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

    private fun getAllNote(){
        flightVM.getAllWishlist().observe(viewLifecycleOwner){
            if (it != null) {
                setDataToRecView(it)
            }
        }
    }

    private fun setDataToRecView(data: List<DataWishList>) {
        binding.apply {
            val adapter = WishListAdapter()
            adapter.setData(data)
            RvWishlist.adapter = adapter
            RvWishlist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}