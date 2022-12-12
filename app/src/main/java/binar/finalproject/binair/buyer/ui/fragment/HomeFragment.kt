package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant.dataUser
import binar.finalproject.binair.buyer.data.model.SearchItem
import binar.finalproject.binair.buyer.data.response.CityAirport
import binar.finalproject.binair.buyer.databinding.FragmentHomeBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity
import binar.finalproject.binair.buyer.ui.adapter.AutoCompleteAirportAdapter
import binar.finalproject.binair.buyer.ui.adapter.HomePromoAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Suppress("UNCHECKED_CAST")
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var flightVM : FlightViewModel
    private val calendar = Calendar.getInstance()
    private var tripType : String = "oneway"
    private lateinit var cityFrom : String
    private lateinit var airportFrom : String
    private lateinit var cityTo : String
    private lateinit var airportTo : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        initData()
        showBottomNavigation()
        showBannerLogin()
        changeTripType()
        setAutoCompleteClass()
        setPromoAdapter()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListener() {
        binding.apply {
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
            }
            etTglBerangkatInput.setOnClickListener {
                showDatePickerDialog("berangkat")
            }
            etTglPulangInput.setOnClickListener {
                showDatePickerDialog("pulang")
            }
            etJmlPenumpangInput.setOnClickListener {
                openDialogPassenger()
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
                    openDialogPassenger()
                }
            }
            btnCari.setOnClickListener {
                searchTicket()
            }
        }
    }

    private fun initData(){
        val now = Calendar.getInstance().time
        val formatedDate = formatDate(now)
        binding.etTglBerangkatInput.setText(formatedDate)
        binding.etTglPulangInput.setText(formatedDate)
    }

    private fun showBottomNavigation() {
        (activity as MainActivity).binding.bottomNavContainer.visibility = View.VISIBLE
    }

    private fun showBannerLogin(){
        val sharedPref = requireActivity().getSharedPreferences(dataUser, 0)
        if(sharedPref.getString("token", null) == null){
            binding.bannerLogin.visibility = View.VISIBLE
        }else{
            binding.bannerLogin.visibility = View.GONE
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
                tripType = "roundtrip"
            }
            cvSekaliJalan.setOnClickListener {
                cvSekaliJalan.setBackgroundColor(Color.parseColor("#13A2D7"))
                tvSekaliJalan.setTextColor(Color.parseColor("#FFFFFF"))
                cvPulangPergi.setBackgroundColor(Color.parseColor("#FFFFFF"))
                tvPulangPergi.setTextColor(Color.parseColor("#7D8C9C"))
                tglPulangInputContainer.visibility = View.GONE
                tripType = "oneway"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAutoCompleteClass() {
        flightVM.callGetCityAirport().observe(viewLifecycleOwner){
            if(it != null){
                val city = it
                val adapter = AutoCompleteAirportAdapter(requireContext(), city as ArrayList<CityAirport?>)
                binding.apply {
                    etFrom.threshold = 1
                    etFrom.setAdapter(adapter)
                    etDestination.threshold = 1
                    etDestination.setAdapter(adapter)
                    etFrom.setOnItemClickListener { adapterView, view, pos, l ->
                        val data = adapter.getDataAirport(pos)
                        cityFrom = data.city
                        airportFrom = data.airport
                        binding.etFrom.setText("${data.city} - ${data.code}")
                    }
                    etDestination.setOnItemClickListener { adapterView, view, pos, l ->
                        val data = adapter.getDataAirport(pos)
                        cityTo = data.city
                        airportTo = data.airport
                        binding.etDestination.setText("${data.city} - ${data.code}")
                    }
                }
            }
        }
    }

    private fun showDatePickerDialog(kategori: String) {
        val datePicker =
            OnDateSetListener { view, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel(kategori,calendar.time)
            }
        DatePickerDialog(requireActivity(),datePicker,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDate(date : Date) : String {
        val myFormat = "EEEE, dd MMM yy"
        val dateFormat = SimpleDateFormat(myFormat)
        return dateFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateAPI(date : String) : String {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("EEEE, dd MMM yy")).toString()
    }

    private fun updateLabel(kategori : String, date : Date) {
        val formatedDate = formatDate(date)
        if(kategori == "berangkat") {
            binding.etTglBerangkatInput.setText(formatedDate)
        } else if(kategori == "pulang"){
            binding.etTglPulangInput.setText(formatedDate)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openDialogPassenger() {
        val bottomSheetFragment = PassengerBottomSheetFragment()
        bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
        flightVM.getTotalPassenger().observe(viewLifecycleOwner){
            if(it != null){
                binding.etJmlPenumpangInput.setText("$it Penumpang")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchTicket() {
        var dateGo = binding.etTglBerangkatInput.text.toString()
        dateGo = formatDateAPI(dateGo)
        var dateBack : String? = null
        if(tripType == "roundtrip") {
            dateBack = binding.etTglPulangInput.text.toString()
            dateBack = formatDateAPI(dateBack)
        }
        var totalPassenger = binding.etJmlPenumpangInput.text.toString().split(" ")[0].toInt()
        val data = SearchItem(cityFrom,airportFrom,cityTo,airportTo, dateGo, dateBack,tripType,totalPassenger)
        val action = HomeFragmentDirections.actionHomeFragmentToListTicketFragment(data)
        findNavController().navigate(action)
    }

    private fun setPromoAdapter() {
        val dataPromo = arrayListOf("Stay Happy Weekly Bersama OCBC NISP, Dapatkan Diskon 12%", "Victorious Tuesday Bersama OCBC NISP, Dapatkan Diskon 12%", "Penerbangan Jadi Menyenangkan Dengan Kartu Kredit Maybank Promo hingga IDR 200.000", "Kesepakatan yang Adil Setiap Hari!", "Promo 5")
        val adapter = HomePromoAdapter(dataPromo)
        binding.rvPromo.adapter = adapter
        binding.rvPromo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}