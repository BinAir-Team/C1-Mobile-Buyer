package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.isOnline
import binar.finalproject.binair.buyer.data.model.SearchItem
import binar.finalproject.binair.buyer.data.response.CityAirport
import binar.finalproject.binair.buyer.data.response.DataPromo
import binar.finalproject.binair.buyer.databinding.FragmentHomeBinding
import binar.finalproject.binair.buyer.ui.activity.MainActivity
import binar.finalproject.binair.buyer.ui.adapter.AutoCompleteAirportAdapter
import binar.finalproject.binair.buyer.ui.adapter.HomePromoAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import binar.finalproject.binair.buyer.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
@Suppress("UNCHECKED_CAST", "SetTextI18n")
@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var flightVM : FlightViewModel
    private val calendar = Calendar.getInstance()
    private var tripType : String = "oneway"
    private var cityFrom : String = "Jakarta"
    private var airportFrom : String = "Soekarno Hatta"
    private var airportFromCode : String = "CGK"
    private var cityTo : String = "Surabaya"
    private var airportTo : String = "Juanda"
    private var airportToCode : String = "SUB"

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
        setupnotification()
    }

    private fun setupnotification(){
        val prefs = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)
        if (token == null){
            binding.toolbar.menuNotif.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListener() {
        binding.toolbar.menuNotif.setOnClickListener {
            val action = HomeFragmentDirections.actionGlobalNotificationFragment("home")
            findNavController().navigate(action)
        }
        binding.apply {
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_global_notificationFragment)
            }
            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
            }
            btnSwitch.setOnClickListener {
                val oldCityFrom = cityFrom
                val oldAirportFrom = airportFrom
                val oldAirportFromCode = airportFromCode
                val oldCityTo = cityTo
                val oldAirportTo = airportTo
                val oldAirportToCode = airportToCode

                cityFrom = oldCityTo ; airportFrom = oldAirportTo ; airportFromCode = oldAirportToCode
                cityTo = oldCityFrom ; airportTo = oldAirportFrom ; airportToCode = oldAirportFromCode

                etFrom.setText("$cityFrom - $airportFromCode")
                etDestination.setText("$cityTo - $airportToCode")
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
            tvLihatSemuaPromo.setOnClickListener {
                findNavController().navigate(R.id.action_global_promoFragment)
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
        val prefs = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE)
        val userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        val token = prefs.getString("token", null)
        val email = prefs.getString("email", null)

        if(token != null){
            if(email != null){
                binding.bannerLogin.visibility = View.GONE
            }
            userVM.getUser("Bearer $token").observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.bannerLogin.visibility = View.GONE
                }else{
                    binding.bannerLogin.visibility = View.VISIBLE
                    prefs.edit().putString("token", null).apply()
                    prefs.edit().clear().apply()
                }
            }
        }else{
            binding.bannerLogin.visibility = View.VISIBLE
            prefs.edit().putString("token", null).apply()
        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
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
        if(isOnline(requireContext())){
            flightVM.callGetCityAirport().observe(viewLifecycleOwner){
                if(it != null){
                    setDataAirport(it)

                    if(!airportInserted()){
                        insertAirportLocal(it)
                    }
                }
            }
        }else{
            flightVM.getAirportLocal().observe(viewLifecycleOwner){
                if(it != null){
                    Log.d("Response",it.toString())
                    setDataAirport(it)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataAirport(airportParam : List<CityAirport>?){
        val adapter = AutoCompleteAirportAdapter(
            requireContext(),
            airportParam as ArrayList<CityAirport?>
        )
        binding.apply {
            etFrom.threshold = 1
            etFrom.setAdapter(adapter)
            etDestination.threshold = 1
            etDestination.setAdapter(adapter)
            etFrom.setOnItemClickListener { adapterView, view, pos, l ->
                val data = adapter.getDataAirport(pos)
                cityFrom = data.city
                airportFrom = data.airport
                airportFromCode = data.code
                binding.etFrom.setText("$cityFrom - $airportFromCode")
            }
            etDestination.setOnItemClickListener { adapterView, view, pos, l ->
                val data = adapter.getDataAirport(pos)
                cityTo = data.city
                airportTo = data.airport
                airportToCode = data.code
                binding.etDestination.setText("$cityTo - $airportToCode")
            }
        }
    }

    private fun airportInserted() : Boolean{
        var res : List<CityAirport>? = null
        flightVM.getAirportLocal().observe(viewLifecycleOwner){
            if (it != null){
                res = it
            }
        }
        return res != null
    }

    private fun insertAirportLocal(listAirport : List<CityAirport>){
        flightVM.insertAirport(listAirport)
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
        val dateFormat = SimpleDateFormat(myFormat, Locale("id", "ID"))
        return dateFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateAPI(date : String) : String {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("EEEE, dd MMM yy",Locale("id", "ID"))).toString()
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
        val totalPassenger = binding.etJmlPenumpangInput.text.toString().split(" ")[0].toInt()
        val data = SearchItem(cityFrom,airportFrom,cityTo,airportTo, dateGo, dateBack,tripType,totalPassenger)
        val action = HomeFragmentDirections.actionHomeFragmentToListTicketFragment(data)
        findNavController().navigate(action)
    }

    private fun validateInput(cityFrom : String, airportFrom : String, cityTo : String, airportTo : String, dateGo : String, dateBack : String) : Boolean{
        return isFilled(cityFrom,airportFrom,cityTo,airportTo,dateGo,dateBack) && isNotSameAirport(cityFrom,airportFrom,cityTo,airportTo) && isNotSameDate(dateGo,dateBack) && isAfterDateGo(dateGo,dateBack)
    }

    private fun isFilled(cityFrom : String, airportFrom : String, cityTo : String, airportTo : String, dateGo : String, dateBack : String) : Boolean{
        if(cityFrom.isEmpty() || airportFrom.isEmpty() || cityTo.isEmpty() || airportTo.isEmpty() || dateGo.isEmpty() || dateBack.isEmpty()){
            return false
        }
        return true
    }

    private fun isNotSameAirport(cityFrom : String, airportFrom : String, cityTo : String, airportTo : String) : Boolean{
        if(cityFrom == cityTo && airportFrom == airportTo){
            return false
        }
        return true
    }

    private fun isNotSameDate(dateGo : String, dateBack : String) : Boolean{
        if(dateGo == dateBack){
            return false
        }
        return true
    }

    private fun isAfterDateGo(dateGo : String, dateBack : String) : Boolean{
        val dateGoFormat = LocalDate.parse(dateGo, DateTimeFormatter.ofPattern("EEEE, dd MMM yy",Locale("id", "ID")))
        val dateBackFormat = LocalDate.parse(dateBack, DateTimeFormatter.ofPattern("EEEE, dd MMM yy",Locale("id", "ID")))
        if(dateGoFormat.isAfter(dateBackFormat)){
            return false
        }
        return true
    }

    private fun setPromoAdapter() {
        if(isOnline(requireContext())){
            flightVM.getAllPromo().observe(viewLifecycleOwner){
                if (it != null){
                    setDataPromoToRV(it)
                    if(!promoInserted()){
                        insertPromoLocal(it)
                    }
                }
            }
        }else{
            flightVM.getPromoLocal().observe(viewLifecycleOwner){
                if(it != null){
                    setDataPromoToRV(it)
                }
            }
        }
    }

    private fun setDataPromoToRV(data : List<DataPromo>){
        val adapter = HomePromoAdapter(data)
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        binding.rvPromo.adapter = adapter
        binding.rvPromo.layoutManager = layoutManager

        adapter.onClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailPromoFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun promoInserted() : Boolean{
        var res : List<DataPromo>? = null
        flightVM.getPromoLocal().observe(viewLifecycleOwner){
            if (it != null){
                res = it
            }
        }
        return res != null
    }

    private fun insertPromoLocal(listAirport : List<DataPromo>){
        flightVM.insertPromo(listAirport)
    }
}