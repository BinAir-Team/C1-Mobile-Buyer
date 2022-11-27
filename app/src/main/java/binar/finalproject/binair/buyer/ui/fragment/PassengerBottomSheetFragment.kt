package binar.finalproject.binair.buyer.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import binar.finalproject.binair.buyer.databinding.BottomSheetPassengerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PassengerBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetPassengerBinding
    private lateinit var sharedPrefPassenger : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = BottomSheetPassengerBinding.inflate(inflater, container, false)
        sharedPrefPassenger = requireActivity().getSharedPreferences("dataPassenger", 0)
        editor = sharedPrefPassenger.edit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

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
//            binding.tvJmlAnak.text = (binding.tvJmlAnak.text.toString().toInt() + 1).toString()
        }

        binding.btnSimpan.setOnClickListener {
            val totalPenumpang = binding.tvJmlDewasa.text.toString().toInt() + binding.tvJmlAnak.text.toString().toInt()
            editor.putInt("totalPenumpang", totalPenumpang)
            editor.apply()
            Toast.makeText(context, "Total penumpang: $totalPenumpang", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }
    }

    companion object {
        const val TAG = "PassengerBottomSheetFragment"
    }
}