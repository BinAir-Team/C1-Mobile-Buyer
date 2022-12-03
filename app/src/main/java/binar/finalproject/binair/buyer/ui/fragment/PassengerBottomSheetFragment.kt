package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.binair.buyer.databinding.BottomSheetPassengerBinding
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PassengerBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetPassengerBinding
    private lateinit var flightVM : FlightViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = BottomSheetPassengerBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOldData()
        setListener()
    }

    @SuppressLint("SetTextI18n")
    private fun setListener() {
        binding.btnMinusDewasa.setOnClickListener {
            if (binding.tvJmlDewasa.text.toString().toInt() > 1) {
                binding.tvJmlDewasa.text = (binding.tvJmlDewasa.text.toString().toInt() - 1).toString()
            }
        }
        binding.btnPlusDewasa.setOnClickListener {
            if(binding.tvJmlDewasa.text.toString().toInt() + binding.tvJmlAnak.text.toString().toInt() < 7){
                binding.tvJmlDewasa.text = (binding.tvJmlDewasa.text.toString().toInt() + 1).toString()
            } else {
                Toast.makeText(context, "Jumlah maksimal pemesanan untuk 7 orang", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMinusAnak.setOnClickListener {
            if (binding.tvJmlAnak.text.toString().toInt() > 0) {
                binding.tvJmlAnak.text = (binding.tvJmlAnak.text.toString().toInt() - 1).toString()
            }
        }
        binding.btnPlusAnak.setOnClickListener {
            if(binding.tvJmlDewasa.text.toString().toInt() + binding.tvJmlAnak.text.toString().toInt() < 7){
                binding.tvJmlAnak.text = (binding.tvJmlAnak.text.toString().toInt() + 1).toString()
            } else {
                Toast.makeText(context, "Jumlah maksimal pemesanan untuk 7 orang", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSimpan.setOnClickListener {
            val jmlDewasa = binding.tvJmlDewasa.text.toString().toInt()
            val jmlAnak = binding.tvJmlAnak.text.toString().toInt()
            val totalPenumpang = jmlDewasa + jmlAnak
//            editor.putInt("jmlDewasa", jmlDewasa)
//            editor.putInt("jmlAnak", jmlAnak)
//            editor.putInt("totalPenumpang", totalPenumpang)
//            editor.apply()
            flightVM.setAdultPassenger(jmlDewasa)
            flightVM.setChildPassenger(jmlAnak)
            flightVM.setTotalPassenger(totalPenumpang)
            this.dismiss()
        }
    }

    private fun getOldData(){
//        val jmlDewasa = sharedPrefPassenger.getInt("jmlDewasa", 1)
//        val jmlAnak = sharedPrefPassenger.getInt("jmlAnak", 0)
        flightVM.getAdultPassenger().observe(viewLifecycleOwner) {
            binding.tvJmlDewasa.text = it.toString()
        }
        flightVM.getChildPassenger().observe(viewLifecycleOwner) {
            binding.tvJmlAnak.text = it.toString()
        }
    }
}