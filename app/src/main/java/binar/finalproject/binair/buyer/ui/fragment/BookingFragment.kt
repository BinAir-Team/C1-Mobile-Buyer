package binar.finalproject.binair.buyer.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant.dataPassenger
import binar.finalproject.binair.buyer.data.local.DataKontak
import binar.finalproject.binair.buyer.data.local.DataPenumpang
import binar.finalproject.binair.buyer.databinding.FormTravelerBinding
import binar.finalproject.binair.buyer.databinding.FragmentBookingBinding

class BookingFragment : Fragment() {
    private lateinit var binding : FragmentBookingBinding
    private lateinit var sharedPrefPassenger : SharedPreferences
    private lateinit var dataKontak : DataKontak
    private lateinit var arrDataPenumpang : ArrayList<DataPenumpang>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        sharedPrefPassenger = requireActivity().getSharedPreferences(dataPassenger, 0)
        binding.toolbar.tvTitlePage.text = "Book"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showFormTraveler()
        setListener()
    }

    private fun showFormTraveler(){
        val jmlDewasa = sharedPrefPassenger.getInt("jmlDewasa", -1)
        val jmlAnak = sharedPrefPassenger.getInt("jmlAnak", -1)

        if(jmlDewasa > 0) {
            for (i in 0 until jmlDewasa) {
                val viewForm = LayoutInflater.from(context).inflate(R.layout.form_traveler, null)
                setMargin(viewForm)
                val formDewasaBinding = FormTravelerBinding.bind(viewForm)
                formDewasaBinding.labelTipeTraveler.text = "(Dewasa ${i + 1})"
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
                binding.formTravelerContainer.addView(viewForm)
            }
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

    private fun setListener() {
        binding.apply {
            btnPesan.setOnClickListener {
                getContactData()
                getTravelerData()
                val action = BookingFragmentDirections.actionBookingFragmentToReviewBookingFragment(arrDataPenumpang.toTypedArray(),dataKontak)
                findNavController().navigate(action)
            }
        }
    }

    private fun getContactData() {
        val nama = binding.etNamaLengkap.text.toString()
        val noTelp = binding.etNoTelp.text.toString()
        val email = binding.etEmail.text.toString()
        dataKontak = DataKontak(nama, noTelp, email)
    }

    private fun getTravelerData() {
        arrDataPenumpang = ArrayList()
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
            val dataPnmp = DataPenumpang(title,name,surname,datebirth,nationality,id_card,no_ktp,type)
            arrDataPenumpang.add(dataPnmp)
        }
    }
}