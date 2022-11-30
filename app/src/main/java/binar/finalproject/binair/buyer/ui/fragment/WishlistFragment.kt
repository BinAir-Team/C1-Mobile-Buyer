package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.local.DataWishList
import binar.finalproject.binair.buyer.viewmodel.WishListViewModel
import binar.finalproject.binair.buyer.databinding.FragmentWishlistBinding
import binar.finalproject.binair.buyer.ui.adapter.WishListAdapter

class WishlistFragment : Fragment(), WishListAdapter.NotesInterface {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private val viewModel : WishListViewModel by viewModels()
    private lateinit var adapter : WishListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.timefilter.setOnClickListener() { v: View ->
            showMenu(v, R.menu.filter_popupmenu)
        }

        adapter = WishListAdapter(this)
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

    fun getAllNote(){
        binding.apply {
            viewModel.getDataNotes().observe(viewLifecycleOwner){
                adapter.setData(it)
                if (it.isEmpty()){
                    tvAlertKosong.visibility = View.VISIBLE
                }
                else
                    tvAlertKosong.visibility = View.GONE
            }
            RvWishlist.adapter = adapter
            RvWishlist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun editNote(notes: DataWishList) {
        TODO("Not yet implemented")
    }

    override fun deleteNote(notes: DataWishList) {
        TODO("Not yet implemented")
    }
}