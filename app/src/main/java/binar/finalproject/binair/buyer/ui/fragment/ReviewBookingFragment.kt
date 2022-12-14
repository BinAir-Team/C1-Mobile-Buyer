package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant.dataUser
import binar.finalproject.binair.buyer.data.formatRupiah
import binar.finalproject.binair.buyer.data.model.DataKontak
import binar.finalproject.binair.buyer.data.model.PostBookingBody
import binar.finalproject.binair.buyer.data.response.BookingTicketResponse
import binar.finalproject.binair.buyer.data.response.TicketItem
import binar.finalproject.binair.buyer.databinding.FragmentReviewBookingBinding
import binar.finalproject.binair.buyer.databinding.ItemTravelerReviewBinding
import binar.finalproject.binair.buyer.databinding.ReviewAlertDialogBinding
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ReviewBookingFragment : Fragment() {
    private lateinit var binding : FragmentReviewBookingBinding
    private lateinit var flightVM : FlightViewModel
    private lateinit var dataKontak : DataKontak
    private lateinit var dataTraveler : PostBookingBody
    private var jmlDewasa : Int = 1
    private var jmlAnak : Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBookingBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        binding.toolbar.tvTitlePage.text = "Review Booking"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataTraveler()
        showDataKontak()
        showDataPenumpang()
        showPrice()
        setListener()
    }

    private fun getDataTraveler() {
        dataKontak = arguments?.getParcelable("dataKontak")!!
        dataTraveler = arguments?.getSerializable("bookingBody") as PostBookingBody
        flightVM.getAdultPassenger().observe(viewLifecycleOwner){
            jmlDewasa = it
        }
        flightVM.getChildPassenger().observe(viewLifecycleOwner){
            jmlAnak = it
        }
    }

    private fun showDataKontak(){
        binding.dataKontakBind = dataKontak
    }

    @SuppressLint("SetTextI18n")
    private fun showDataPenumpang(){
        var counterDewasa = 1
        var counterAnak = 1
        for(data in dataTraveler.traveler){
            val viewForm = LayoutInflater.from(context).inflate(R.layout.item_traveler_review, null)
            val itemBinding = ItemTravelerReviewBinding.bind(viewForm)
            if(data.type == "adult"){
                itemBinding.labelDetailPenumpang.text = "Detail Penumpang (Dewasa $counterDewasa)"
                itemBinding.tvNama.text = data.name
                itemBinding.tvNoIdnt.text = data.noKtp
                counterDewasa++
            } else {
                itemBinding.labelDetailPenumpang.text = "Detail Penumpang (Anak $counterAnak)"
                itemBinding.tvNama.text = data.name
                itemBinding.labelNoIdnt.visibility = View.GONE
                itemBinding.tvNoIdnt.visibility = View.GONE
                counterAnak++
            }
            binding.containerDataTraveler.addView(viewForm)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showPrice(){
        var ticket : TicketItem
        flightVM.getChosenTicket().observe(viewLifecycleOwner) {
            if (it != null) {
                ticket = it
                val totalPriceAdult = ticket.adultPrice * jmlDewasa
                val totalPriceChild = ticket.childPrice * jmlAnak
                binding.apply {
                    tvTotalDewasa.text = "x$jmlDewasa"
                    tvTotalHargaDewasa.text = formatRupiah(totalPriceAdult)
                    if (jmlAnak != 0 ){
                        containerAnak.visibility = View.VISIBLE
                        tvTotalAnak.text = "x$jmlAnak"
                        tvTotalHargaAnak.text = formatRupiah(totalPriceChild)
                    }
                    tvTotalHarga.text = formatRupiah(totalPriceAdult + totalPriceChild)
                }
            }
        }
    }

    private fun setListener(){
        binding.btnBayar.setOnClickListener {
            displayReviewAlert()
        }
    }

    private fun displayReviewAlert(){
        val builder = AlertDialog.Builder(context)
        val alertDialog = builder.create()
        val dialogView = layoutInflater.inflate(R.layout.review_alert_dialog, null)
        val dialogBinding = ReviewAlertDialogBinding.bind(dialogView)
        alertDialog.setView(dialogView)
        alertDialog.create()
        dialogBinding.btnPayment.setOnClickListener {
            val data = bookTicket()
            alertDialog.dismiss()
        }
        dialogBinding.btnBack.setOnClickListener {
            findNavController().popBackStack()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun bookTicket() : BookingTicketResponse?{
        val prefs = requireActivity().getSharedPreferences(dataUser, 0)
        val token = prefs.getString("token", null)
        val body = arguments?.getSerializable("bookingBody") as PostBookingBody
        var result : BookingTicketResponse? = null
        if (token != null) {
            flightVM.bookTicket("Bearer $token", body).observe(viewLifecycleOwner){
                if (it != null) {
                    result = it
                    findNavController().navigate(ReviewBookingFragmentDirections.actionReviewBookingFragmentToPaymentFragment(it))
                }
            }
        }
        return result
    }
}