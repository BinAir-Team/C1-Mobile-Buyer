package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant.dataUser
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.databinding.FragmentProfileBinding
import binar.finalproject.binair.buyer.ui.adapter.WishListAdapter
import binar.finalproject.binair.buyer.viewmodel.UserViewModel
import binar.finalproject.binair.buyer.viewmodel.WishListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), WishListAdapter.NotesInterface {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel : WishListViewModel by viewModels()
    private lateinit var adapter : WishListAdapter
    lateinit var userVM : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WishListAdapter(this)
        setListener()
        getDataUser()
        getAllNote()
    }

    private fun setListener(){
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
            btnLogout.setOnClickListener {
                logout()
            }
        }
    }

    private fun gotoEditProfile() {
        findNavController().navigate(R.id.action_profileFragment2_to_editProfileFragment)
    }

    @SuppressLint("SetTextI18n")
    private fun getDataUser(){
        val prefs = requireActivity().getSharedPreferences(dataUser, 0)
        val name = prefs.getString("namaLengkap", "Login untuk melanjutkan")
        binding.tvName.text = name
    }

    private fun logout(){
        val prefs = requireActivity().getSharedPreferences(dataUser, 0)
        var token = prefs.getString("token", null)
        token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImFiY2E3ZmE2LWI0MmUtNDVhNy05ZWE2LWYzMjI0OWNkMTJhOCIsImZpcnN0bmFtZSI6IlJpY2hhcmQiLCJsYXN0bmFtZSI6IkxvaXMiLCJnZW5kZXIiOiJMYWtpLWxha2kiLCJlbWFpbCI6InJpY2hhcmRAZ21haWwuY29tIiwicGhvbmUiOiIwODEyMzQ1Njc4OTEiLCJyb2xlIjoibWVtYmVyIiwicHJvZmlsZV9pbWFnZSI6Imh0dHBzOi8vd3d3LmtpbmRwbmcuY29tL3BpY2MvbS8yMS0yMTQ0MzlfZnJlZS1oaWdoLXF1YWxpdHktcGVyc29uLWljb24tZGVmYXVsdC1wcm9maWxlLXBpY3R1cmUucG5nIiwiaWF0IjoxNjcwNTg4OTgyLCJleHAiOjE2NzA1OTI1ODJ9.CJLO6fh3xEoj0EML8AwkGt6s4EFLjbthPub6Dr1vaik"
        if(token != ""){
            Log.d("token", token)
            val alert = AlertDialog.Builder(requireContext())
            alert.apply {
                setTitle("Logout")
                setMessage("Apakah anda yakin ingin logout?")
                setPositiveButton("Ya") { dialog, _ ->
//                    userVM.logout(token).observe(viewLifecycleOwner) {
//                        if (it!!.status == "success") {
//                            Toast.makeText(requireContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show()
//                            val editor = prefs.edit()
//                            editor.clear()
//                            editor.apply()
//                            getDataUser()
//                            findNavController().navigate(R.id.action_global_homeFragment)
//                        }
//                    }
                    Toast.makeText(requireContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show()
                    val editor = prefs.edit()
                    editor.clear()
                    editor.apply()
                    getDataUser()
                    findNavController().navigate(R.id.action_global_homeFragment)
                    dialog.dismiss()
                }
                setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            alert.create().show()
        } else {
            Toast.makeText(requireContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    fun addnote(){
        binding.apply {
            val flightNumber = binding.FlightNumber.text.toString()
            val timeDepart = binding.JamTerbang.text.toString()
            val destination = binding.Tujuan.text.toString()
            val progress = binding.ProgressBar.text.toString()

            if (flightNumber!!.isEmpty()  || timeDepart!!.isEmpty() || destination!!.isEmpty() || progress.toString()!!.isEmpty()){
                Toast.makeText(context, "Anda belum mengisi note", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.addWishlist(DataWishList(0,flightNumber,timeDepart,destination,progress))
                Toast.makeText(context, "Note tersimpan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getAllNote(){
        binding.apply {
            viewModel.getDataWishlist().observe(viewLifecycleOwner){
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