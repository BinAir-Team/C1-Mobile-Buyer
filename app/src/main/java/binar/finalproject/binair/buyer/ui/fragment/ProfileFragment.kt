package binar.finalproject.binair.buyer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.local.DataWishList
import binar.finalproject.binair.buyer.databinding.FragmentProfileBinding
import binar.finalproject.binair.buyer.ui.adapter.WishListAdapter
import binar.finalproject.binair.buyer.viewmodel.WishListViewModel

class ProfileFragment : Fragment(), WishListAdapter.NotesInterface {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel : WishListViewModel by viewModels()
    private lateinit var adapter : WishListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.AddButton.setOnClickListener() {
            addnote()
        }
        binding.apply {
            tvName.setOnClickListener {
                gotoEditProfile()
            }
            userprofile.setOnClickListener {
                gotoEditProfile()
            }
        }
        adapter = WishListAdapter(this)
        getAllNote()
    }

    private fun gotoEditProfile() {
        findNavController().navigate(R.id.action_profileFragment2_to_editProfileFragment)
    }

    fun addnote(){
        binding.apply {
            var flightNumber = binding.FlightNumber.text.toString()
            var timeDepart = binding.JamTerbang.text.toString()
            var destination = binding.Tujuan.text.toString()
            var progress = binding.ProgressBar.text.toString()

            if (flightNumber!!.isEmpty()  || timeDepart!!.isEmpty() || destination!!.isEmpty() || progress.toString()!!.isEmpty()){
                Toast.makeText(context, "Anda belum mengisi note", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.addNote(DataWishList(0,flightNumber,timeDepart,destination,progress))
                Toast.makeText(context, "Note tersimpan", Toast.LENGTH_SHORT).show()
            }
        }
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