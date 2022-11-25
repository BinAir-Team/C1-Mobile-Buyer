package binar.finalproject.binair.buyer.ui.fragment

import android.R
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.databinding.FragmentHomeBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity
import binar.finalproject.binair.buyer.ui.adapter.HomePromoAdapter
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val calendar = Calendar.getInstance()
    private lateinit var sharedPrefPassenger : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedPrefPassenger = requireActivity().getSharedPreferences("dataPassenger", 0)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation()
        setListener()
        setAutoCompleteClass()
        setPromoAdapter()
        changeTripType()
    }

    private fun setPromoAdapter() {
        val dataPromo = arrayListOf("Stay Happy Weekly Bersama OCBC NISP, Dapatkan Diskon 12%", "Victorious Tuesday Bersama OCBC NISP, Dapatkan Diskon 12%", "Penerbangan Jadi Menyenangkan Dengan Kartu Kredit Maybank Promo hingga IDR 200.000", "Kesepakatan yang Adil Setiap Hari!", "Promo 5")
        val adapter = HomePromoAdapter(dataPromo)
        binding.rvPromo.adapter = adapter
        binding.rvPromo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setListener() {
        binding.apply {
            etTglBerangkatInput.setOnFocusChangeListener { view, b ->
                if (b) {
                    showDatePickerDialog("berangkat")
                }
            }
            etTglPulangInput.setOnFocusChangeListener { view, b ->
                if (b) {
                    showDatePickerDialog("pulang")
                }
            }
            etJmlPenumpangInput.setOnFocusChangeListener { view, b ->
                if (b) {
                    setPassenger()
                }
            }
        }
    }

    private fun setPassenger() {
        openDialogPassenger()
    }

    private fun openDialogPassenger() {
        val bottomSheetFragment = PassengerBottomSheetFragment()
        bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
        val totalPenumpang = sharedPrefPassenger.getInt("totalPenumpang", 0)
        binding.etJmlPenumpangInput.setText("$totalPenumpang")
    }

    private fun showDatePickerDialog(kategori: String) {
        val datePicker =
            OnDateSetListener { view, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel(kategori)
            }
        DatePickerDialog(requireActivity(),datePicker,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private fun updateLabel(kategori : String) {
        val myFormat = "dd/MM/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        if(kategori == "berangkat") {
            binding.etTglBerangkatInput.setText(dateFormat.format(calendar.time))
        } else if(kategori == "pulang"){
            binding.etTglPulangInput.setText(dateFormat.format(calendar.time))
        }
    }

    private fun changeTripType() {
        binding.apply {
            cvPulangPergi.setOnClickListener {
                cvPulangPergi.setBackgroundColor(Color.parseColor("#13A2D7"))
                tvPulangPergi.setTextColor(Color.parseColor("#FFFFFF"))
                cvSekaliJalan.setBackgroundColor(Color.parseColor("#FFFFFF"))
                tvSekaliJalan.setTextColor(Color.parseColor("#7D8C9C"))
                tglPulangInputContainer.visibility = View.VISIBLE
            }
            cvSekaliJalan.setOnClickListener {
                cvSekaliJalan.setBackgroundColor(Color.parseColor("#13A2D7"))
                tvSekaliJalan.setTextColor(Color.parseColor("#FFFFFF"))
                cvPulangPergi.setBackgroundColor(Color.parseColor("#FFFFFF"))
                tvPulangPergi.setTextColor(Color.parseColor("#7D8C9C"))
                tglPulangInputContainer.visibility = View.GONE
            }
        }
    }

    private fun setAutoCompleteClass() {
        val ticketsClass = arrayOf("Ekonomi","Bisnis","First Class")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.select_dialog_item, ticketsClass)
        val atcClas = binding.etKelasInput
        atcClas.threshold = 1
        atcClas.setAdapter(adapter)
    }

    private fun showBottomNavigation() {
        (activity as MainActivity).binding.bottomNavContainer.visibility = View.VISIBLE
//        (activity as MainActivity).binding.bottomNav.selectedItemId = binar.finalproject.binair.buyer.R.id.invisibleMenu
    }
}