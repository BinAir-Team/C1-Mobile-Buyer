package binar.finalproject.binair.buyer.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant.dataPassenger
import binar.finalproject.binair.buyer.data.model.DataKontak
import binar.finalproject.binair.buyer.data.model.PostBookingBody
import binar.finalproject.binair.buyer.data.model.Quantity
import binar.finalproject.binair.buyer.data.model.TravelerItem
import binar.finalproject.binair.buyer.databinding.FormTravelerBinding
import binar.finalproject.binair.buyer.databinding.FragmentBookingBinding
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingFragment : Fragment() {
    private lateinit var binding : FragmentBookingBinding
    private lateinit var flightVM : FlightViewModel
    private var jmlDewasa : Int = 0
    private var jmlAnak : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        binding.toolbar.tvTitlePage.setText("Book")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flightVM.getAdultPassenger().observe(viewLifecycleOwner){
            jmlDewasa = it
        }
        flightVM.getChildPassenger().observe(viewLifecycleOwner){
            jmlAnak = it
        }
        setListener()
        showFormTraveler()
    }

    private fun setListener() {
        binding.apply {
            btnPesan.setOnClickListener {
                bookTicket()
            }
        }
    }

    private fun showFormTraveler(){
        jmlDewasa = 1
        if(jmlDewasa > 0) {
            Toast.makeText(requireContext(), "Dewasa", Toast.LENGTH_SHORT).show()
            for (i in 0 until jmlDewasa) {
                val viewForm = LayoutInflater.from(context).inflate(R.layout.form_traveler, null)
                setMargin(viewForm)
                val formDewasaBinding = FormTravelerBinding.bind(viewForm)
                formDewasaBinding.labelTipeTraveler.text = "(Dewasa ${i + 1})"
                validateInputForm(formDewasaBinding,"dewasa")
                binding.formTravelerContainer.addView(viewForm)
            }
        }
        if(jmlAnak > 0){
            for (i in 0 until jmlAnak) {
                val viewForm = LayoutInflater.from(context).inflate(R.layout.form_traveler, null)
                setMargin(viewForm)
                val formAnakBinding = FormTravelerBinding.bind(viewForm)
                formAnakBinding.labelTipeTraveler.text = "(Anak ${i + 1})"
                formAnakBinding.containerTipeIdnt.visibility = View.GONE
                formAnakBinding.containerNoIdentitas.visibility = View.GONE
                validateInputForm(formAnakBinding,"anak")
                binding.formTravelerContainer.addView(viewForm)
            }
        }
    }

    private fun validateInputForm(binding : FormTravelerBinding, kategori : String){
        binding.apply {
            etTipe.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if(etTipe.text.isNullOrEmpty()){
                        etTitel.error = "Tipe tidak boleh kosong"
                    }
                }
            })
            etKewarganegaraan.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if(etKewarganegaraan.text.isNullOrEmpty()){
                        etKewarganegaraan.error = "Kewarganegaraan tidak boleh kosong"
                    }
                }
            })
            if(kategori == "dewasa"){
                etTipe.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        if(etTipe.text.isNullOrEmpty()){
                            etTipe.error = "Tipe tidak boleh kosong"
                        }
                    }
                })
                etNoIdnt.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        if(etNoIdnt.text.isNullOrEmpty()){
                            etNoIdnt.error = "No Identitas tidak boleh kosong"
                        }
                    }
                })
            }
            etNamaDepanAdlt.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if(etNamaDepanAdlt.text.isNullOrEmpty()){
                        etNamaDepanAdlt.error = "Nama Depan tidak boleh kosong"
                    }
                }
            })
            etNamaBelakangAdlt.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if(etNamaBelakangAdlt.text.isNullOrEmpty()){
                        etNamaBelakangAdlt.error = "Nama Belakang tidak boleh kosong"
                    }
                }
            })
            etTglLahir.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if(etTglLahir.text.isNullOrEmpty()){
                        etTglLahir.error = "Tanggal Lahir tidak boleh kosong"
                    }
                }
            })
        }
    }

    private fun setMargin(v : View){
        v.layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT,
            ViewGroup.MarginLayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(24, 16, 24, 16)
        }
    }

    private fun getContactData() : DataKontak{
        val nama = binding.etNamaLengkap.text.toString()
        val noTelp = binding.etNoTelp.text.toString()
        val email = binding.etEmail.text.toString()
        return DataKontak(nama, noTelp, email)
    }

    private fun getTravelerData() : ArrayList<TravelerItem>{
        val arrDataPenumpang = ArrayList<TravelerItem>()
        binding.formTravelerContainer.children.forEach {
            val formTravelerBinding = FormTravelerBinding.bind(it)
            val title = formTravelerBinding.etTitel.text.toString()
            val name = formTravelerBinding.etNamaDepanAdlt.text.toString()
            val surname = formTravelerBinding.etNamaBelakangAdlt.text.toString()
            val datebirth = formTravelerBinding.etTglLahir.text.toString()
            val nationality = formTravelerBinding.etKewarganegaraan.text.toString()
            var id_card : String? = null
            var no_ktp : String? = null
            var type : String
            if(formTravelerBinding.labelTipeTraveler.text.contains("Dewasa")){
                id_card = formTravelerBinding.etNoIdnt.text.toString()
                no_ktp = formTravelerBinding.etNoIdnt.text.toString()
                type =  "adult"
            }else{
                type =  "child"
            }
            val dataPnmp = TravelerItem(datebirth,nationality,surname,no_ktp,name,id_card,type,title)
            arrDataPenumpang.add(dataPnmp)
        }
        return arrDataPenumpang
    }

    private fun bookTicket(){
        val dataKontak = getContactData()
        val dataTrav = getTravelerData()
//        val action = BookingFragmentDirections.actionBookingFragmentToReviewBookingFragment(arrDataPenumpang.toTypedArray(),dataKontak)
//        findNavController().navigate(action)
        val userPrefs = requireActivity().getSharedPreferences(dataPassenger, Context.MODE_PRIVATE)
        val token = userPrefs.getString("token", null)
        if(token != null){
            val idTicket = arguments?.getString("idTicket")
            val qtt = Quantity(adult = jmlDewasa, child = jmlAnak)
            val body = idTicket?.let { PostBookingBody(it,qtt,dataTrav,"Dana") }
            if (body != null) {
                val action = BookingFragmentDirections.actionBookingFragmentToReviewBookingFragment(dataKontak,body)
                findNavController().navigate(action)
//                flightVM.bookTicket(token, body).observe(viewLifecycleOwner){
//                    if(it != null){
//                        Toast.makeText(requireContext(), "Pemesanan Berhasil", Toast.LENGTH_SHORT).show()
//                        findNavController().navigate(R.id.action_bookingFragment_to_reviewBookingFragment)
//                    }
//                }
            }
        }
    }
}