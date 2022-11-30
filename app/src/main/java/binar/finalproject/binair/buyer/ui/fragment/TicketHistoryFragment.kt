package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.databinding.FragmentProfileBinding
import binar.finalproject.binair.buyer.databinding.FragmentTicketHistoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class TicketHistoryFragment : Fragment() {
    private lateinit var binding : FragmentTicketHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTicketHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.timefilter.setOnClickListener(){
                v: View ->
            showMenu(v, R.menu.filter_popupmenu)
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
}