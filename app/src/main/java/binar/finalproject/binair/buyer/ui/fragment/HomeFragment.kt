package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.data.Constant.dataPassenger
import binar.finalproject.binair.buyer.data.response.CityAirport
import binar.finalproject.binair.buyer.databinding.FragmentHomeBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity
import binar.finalproject.binair.buyer.ui.adapter.AutoCompleteAirportAdapter
import binar.finalproject.binair.buyer.ui.adapter.HomePromoAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var flightVM : FlightViewModel
    private val calendar = Calendar.getInstance()
    private lateinit var sharedPrefPassenger : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        sharedPrefPassenger = requireActivity().getSharedPreferences(dataPassenger, 0)
        editor = sharedPrefPassenger.edit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDate()
        showBottomNavigation()
        clearTotalPassenger()
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
            etTglBerangkatInput.setOnClickListener {
                showDatePickerDialog("berangkat")
            }
            etTglPulangInput.setOnClickListener {
                showDatePickerDialog("pulang")
            }
            etJmlPenumpangInput.setOnClickListener {
                setPassenger()
            }
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
            btnCari.setOnClickListener {
                searchTicket()
            }
        }
    }

    private fun searchTicket() {
        findNavController().navigate(binar.finalproject.binair.buyer.R.id.action_homeFragment_to_listTicketFragment)
    }

    private fun clearTotalPassenger() {
        editor.remove("jmlAnak")
        editor.putInt("jmlDewasa",1)
        editor.putInt("totalPenumpang", 1)
        editor.apply()
    }

    private fun setPassenger() {
        openDialogPassenger()
    }

    @SuppressLint("SetTextI18n")
    private fun openDialogPassenger() {
        val bottomSheetFragment = PassengerBottomSheetFragment()
        bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
        val totalPenumpang = sharedPrefPassenger.getInt("totalPenumpang", 0)
        binding.etJmlPenumpangInput.setText("$totalPenumpang Penumpang")
    }

    private fun initDate(){
        val now = Calendar.getInstance().time
        val formatedDate = formatDate(now)
        binding.etTglBerangkatInput.setText(formatedDate)
        binding.etTglPulangInput.setText(formatedDate)
    }

    private fun showDatePickerDialog(kategori: String) {
        val datePicker =
            OnDateSetListener { view, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel(kategori,calendar.time)
            }
        DatePickerDialog(requireActivity(),datePicker,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private fun formatDate(date : Date) : String {
        val myFormat = "EEEE, dd MMM yy"
        val dateFormat = SimpleDateFormat(myFormat)
        return dateFormat.format(date)
    }

    private fun updateLabel(kategori : String, date : Date) {
        val formatedDate = formatDate(date)
        if(kategori == "berangkat") {
            binding.etTglBerangkatInput.setText(formatedDate)
        } else if(kategori == "pulang"){
            binding.etTglPulangInput.setText(formatedDate)
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
        flightVM.callGetCityAirport().observe(viewLifecycleOwner){
            if(it != null){
                var city = it
                val adapter = AutoCompleteAirportAdapter(requireContext(), city as ArrayList<CityAirport?>)
                binding.apply {
                    etFrom.threshold = 1
                    etFrom.setAdapter(adapter)
                    etDestination.threshold = 1
                    etDestination.setAdapter(adapter)
                    etFrom.setOnItemClickListener { adapterView, view, pos, l ->
                        val data = adapter.getDataAirport(pos)
                        binding.etFrom.setText("${data.city} - ${data.code}")
                    }
                    etDestination.setOnItemClickListener { adapterView, view, pos, l ->
                        val data = adapter.getDataAirport(pos)
                        binding.etDestination.setText("${data.city} - ${data.code}")
                    }
                }
            }
        }
    }

    private fun onClickAutoCompleteListener(){
        binding.etFrom.setOnItemClickListener { adapterView, view, pos, id ->
            val data = adapterView.getItemAtPosition(pos) as CityAirport
            binding.etFrom.setText("${data.city} - ${data.code}")
            binding.etFrom.clearFocus()
            binding.etDestination.requestFocus()
        }

        binding.etDestination.setOnItemClickListener { adapterView, view, pos, id ->
            val data = adapterView.getItemAtPosition(pos) as CityAirport
            binding.etDestination.setText("${data.city} - ${data.code}")
            binding.etDestination.clearFocus()
            binding.etTglBerangkatInput.requestFocus()
        }
    }

    private fun showBottomNavigation() {
        (activity as MainActivity).binding.bottomNavContainer.visibility = View.VISIBLE
    }
}