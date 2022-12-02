package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.model.DataKontak
import binar.finalproject.binair.buyer.data.model.DataPenumpang
import binar.finalproject.binair.buyer.databinding.FragmentReviewBookingBinding
import binar.finalproject.binair.buyer.databinding.ItemTravelerReviewBinding
import binar.finalproject.binair.buyer.databinding.ReviewAlertDialogBinding

class ReviewBookingFragment : Fragment() {
    private lateinit var binding : FragmentReviewBookingBinding
    private lateinit var dataKontak : DataKontak
    private lateinit var dataTraveler : List<Parcelable>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBookingBinding.inflate(inflater, container, false)
        binding.toolbar.tvTitlePage.text = "Review Booking"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataTraveler()
        showDataKontak()
        showDataPenumpang()
        setListener()
    }

    private fun getDataTraveler() {
        dataKontak = arguments?.getParcelable("dataKontak")!!
        dataTraveler = arguments?.getParcelableArray("dataPenumpang")!!.toList()
    }

    private fun showDataKontak(){
        binding.dataKontakBind = dataKontak
    }

    @SuppressLint("SetTextI18n")
    private fun showDataPenumpang(){
        var counterDewasa = 1
        var counterAnak = 1
        for(data in dataTraveler){
            val viewForm = LayoutInflater.from(context).inflate(R.layout.item_traveler_review, null)
            val itemBinding = ItemTravelerReviewBinding.bind(viewForm)
            if((data as DataPenumpang).type == "adult"){
                itemBinding.labelDetailPenumpang.text = "Detail Penumpang (Dewasa $counterDewasa)"
                itemBinding.tvNama.text = data.name
                itemBinding.tvNoIdnt.text = data.no_ktp
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
            findNavController().navigate(R.id.action_reviewBookingFragment_to_paymentFragment)
            alertDialog.dismiss()
        }
        dialogBinding.btnBack.setOnClickListener {
            findNavController().popBackStack()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}