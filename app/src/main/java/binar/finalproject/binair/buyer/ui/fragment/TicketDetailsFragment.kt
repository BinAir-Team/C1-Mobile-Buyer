package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.Constant.dataUser
import binar.finalproject.binair.buyer.data.formatRupiah
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.data.response.TicketItem
import binar.finalproject.binair.buyer.databinding.FragmentTicketDetailsBinding
import binar.finalproject.binair.buyer.databinding.LoginAlertDialogBinding
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class TicketDetailsFragment : Fragment() {
    private lateinit var binding : FragmentTicketDetailsBinding
    private lateinit var clickedTicket : TicketItem
    private lateinit var flightVM : FlightViewModel
    private lateinit var sharedPrefs : SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTicketDetailsBinding.inflate(inflater, container, false)
        binding.toolbar.tvTitlePage.text = "Detail Penerbangan"
        sharedPrefs = requireActivity().getSharedPreferences(dataUser, 0)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromBundle()
        setListener()

    }

    private fun getDataFromBundle() {
        flightVM.getChosenTicket().observe(viewLifecycleOwner){
            if (it != null) {
                clickedTicket = it
                setDataToView()
                bookmarkinit()
            }
        }
    }


    private fun setDataToView() {
        if(clickedTicket.dateEnd == null){
            clickedTicket.dateEnd = ""
            binding.apply {
                labelTglPulang.visibility = View.GONE
                tvTglPulang.visibility = View.GONE
            }
        }
        binding.ticket = clickedTicket
        binding.tvHargaDewasa.text = formatRupiah(clickedTicket.adultPrice)
        binding.tvHargaAnak.text = formatRupiah(clickedTicket.childPrice)
    }

    private fun setListener() {
        binding.btnPesan.setOnClickListener {
            if(sharedPrefs.getString("token", null) == null) {
                displayLoginAlert()
            } else {
                val action = TicketDetailsFragmentDirections.actionTicketDetailsFragmentToBookingFragment(clickedTicket.id)
                findNavController().navigate(action)
            }
        }
        binding.apply {
            val idUser = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE).getString("idUser",null)
            if(idUser != null){
                flightVM.getChosenTicket().observe(viewLifecycleOwner){
                    val dataWishlist = it?.let { it1 ->
                        DataWishList(
                            0,
                            it.dateStart,
                            it.arrivalTime,
                            it.airportTo,
                            it.childPrice,
                            it.adultPrice,
                            it.airportFrom,
                            it.from,
                            it.dateEnd,
                            it.id,
                            it.to,
                            it.type,
                            it1.departureTime,
                            idUser
                        )

                    }
                    if (dataWishlist != null) {
                        binding.btnWishlist.setOnClickListener(){
                            flightVM.insertWishList(dataWishlist,idUser)
                            showbookmark(true)
                            Toast.makeText(context, "WishList Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                        binding.btnRemoveWishlist.setOnClickListener(){
                            flightVM.deleteWishList(dataWishlist.id)
                            showbookmark(false)
                            Toast.makeText(context, "WishList Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.btnWishlist.setOnClickListener(){
            val idUser = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE).getString("idUser",null)
            if(idUser != null){
                flightVM.getChosenTicket().observe(viewLifecycleOwner){
                    val dataWishlist = it?.let { it1 ->
                        DataWishList(
                            0,
                            it.dateStart,
                            it.arrivalTime,
                            it.airportTo,
                            it.childPrice,
                            it.adultPrice,
                            it.airportFrom,
                            it.from,
                            it.dateEnd,
                            it.id,
                            it.to,
                            it.type,
                            it1.departureTime,
                            idUser
                        )

                    }
                    if (dataWishlist != null) {
                            Toast.makeText(context, "WishList Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                            flightVM.insertWishList(dataWishlist,idUser)
//                            flightVM.deleteWishList(dataWishlist)
                    }
                }
            }
        }
    }

    private fun displayLoginAlert(){
        val builder = AlertDialog.Builder(context)
        val alertDialog = builder.create()
        val dialogView = layoutInflater.inflate(R.layout.login_alert_dialog, null)
        val dialogBinding = LoginAlertDialogBinding.bind(dialogView)
        alertDialog.setView(dialogView)
        alertDialog.create()
        dialogBinding.btnLoginAlert.setOnClickListener {
            findNavController().navigate(R.id.action_ticketDetailsFragment_to_loginFragment)
            alertDialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun bookmarkinit(){
        val idUser = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE).getString("idUser",null)
        if (clickedTicket != null && idUser != null) {
            val condition : Boolean
            condition = flightVM.isWishlisted(clickedTicket.id,idUser)
//            Toast.makeText(context,condition.toString(),Toast.LENGTH_SHORT).show()
            showbookmark(condition)
        }
    }

    private fun showbookmark(condition : Boolean) {
        if (condition == true) {
            binding.btnWishlist.visibility = View.GONE
            binding.btnRemoveWishlist.visibility = View.VISIBLE
        } else {
            binding.btnWishlist.visibility = View.VISIBLE
            binding.btnRemoveWishlist.visibility = View.GONE
        }
    }
}
