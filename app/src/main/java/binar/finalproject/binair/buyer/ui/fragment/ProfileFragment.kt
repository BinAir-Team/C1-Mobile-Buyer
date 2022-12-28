package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant.dataUser
import binar.finalproject.binair.buyer.databinding.FragmentProfileBinding
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import binar.finalproject.binair.buyer.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userVM : UserViewModel
    private lateinit var flightVM : FlightViewModel
    private lateinit var prefs : SharedPreferences
    private var token : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        flightVM = ViewModelProvider(this).get(FlightViewModel::class.java)
        prefs = requireActivity().getSharedPreferences(dataUser, 0)
        token = prefs.getString("token", null)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        getDataUser()
//        getWishlist()
    }

    private fun setListener(){
        binding.apply {
            tvName.setOnClickListener {
                if(token != null){
                    gotoEditProfile()
                }else{
                    Toast.makeText(requireContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
            userprofile.setOnClickListener {
                if(token != null){
                    gotoEditProfile()
                }else{
                    Toast.makeText(requireContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
            btnLogout.setOnClickListener {
                logout()
            }
            tvUpdateProfile.setOnClickListener {
                gotoEditProfile()
            }
            tvChangePassword.setOnClickListener {
                gotoChangePassword()
            }
            tvWishlist.setOnClickListener {
                gotoWishlist()
            }
            tvTransaction.setOnClickListener {
                gotoTransaction()
            }
            toolbar.menuNotif.visibility = View.GONE
//            toolbar.setOnMenuItemClickListener {
//                when(it.itemId){
//                    R.id.change_pass -> {
//                        if(token != null){
//                            gotoChangePassword()
//                        }else{
//                            Toast.makeText(requireContext(), "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
//                        }
//                        true
//                    }
//                    else -> false
//                }
//            }
        }
    }

    private fun gotoEditProfile() {
        findNavController().navigate(R.id.action_profileFragment2_to_editProfileFragment)
    }

    private fun gotoChangePassword() {
        findNavController().navigate(R.id.action_profileFragment2_to_changePasswordFragment)
    }

    private fun gotoWishlist() {
        findNavController().navigate(R.id.action_global_wishlistFragment2)
    }

    private fun gotoTransaction() {
        findNavController().navigate(R.id.action_global_ticketHistoryFragment2)
    }

    @SuppressLint("SetTextI18n")
    private fun getDataUser(){
        showLoadingProfile(true)
        if(token != null){
            userVM.getUser("Bearer $token").observe(viewLifecycleOwner) {
                if (it != null) {
                    showLoadingProfile(false)
                    binding.tvName.text = it.data.firstname + " " + it.data.lastname
                    Glide.with(requireContext())
                        .load(it.data.profileImage)
                        .into(binding.userprofile)
                }else{
                    showLoadingProfile(false)
                    binding.tvName.text = "Login untuk melanjutkan"
                    binding.btnLogout.visibility = View.GONE
                    binding.userprofile.setImageResource(R.drawable.ic_profile)
                }
            }
        }else{
            showLoadingProfile(false)
            binding.tvName.text = "Login untuk melanjutkan"
            binding.btnLogout.visibility = View.GONE
            binding.userprofile.setImageResource(R.drawable.ic_profile)
        }
    }

    private fun logout(){
        if(token != null){
            val alert = AlertDialog.Builder(requireContext())
            alert.apply {
                setTitle("Logout")
                setMessage("Apakah anda yakin ingin logout?")
                setPositiveButton("Ya") { dialog, _ ->
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

//    private fun getWishlist(){
//        val idUser = requireActivity().getSharedPreferences(dataUser, Context.MODE_PRIVATE).getString("idUser",null)
//        if (idUser != null) {
//            flightVM.getAllWishlist(idUser).observe(viewLifecycleOwner) {
//                if(it != null){
//                    val adapter = WishListAdapter()
//                    adapter.setData(it)
//                    binding.rvWishlist.adapter = adapter
//                    binding.rvWishlist.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
//                }
//            }
//        }
//    }

    private fun showLoadingProfile(state : Boolean) {
        if(state){
            binding.shimmerContainer.visibility = View.VISIBLE
            binding.shimmerContainer.startShimmerAnimation()
        }else{
            binding.shimmerContainer.visibility = View.GONE
            binding.shimmerContainer.stopShimmerAnimation()
        }
    }
}