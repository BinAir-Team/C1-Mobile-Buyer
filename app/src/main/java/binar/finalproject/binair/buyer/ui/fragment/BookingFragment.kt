package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.model.DataKontak
import binar.finalproject.binair.buyer.data.model.PostBookingBody
import binar.finalproject.binair.buyer.data.model.Quantity
import binar.finalproject.binair.buyer.data.model.TravelerItem
import binar.finalproject.binair.buyer.databinding.FormTravelerBinding
import binar.finalproject.binair.buyer.databinding.FragmentBookingBinding
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import binar.finalproject.binair.buyer.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class BookingFragment : Fragment() {
    private lateinit var binding: FragmentBookingBinding
    private lateinit var flightVM: FlightViewModel
    private var jmlDewasa: Int = 0
    private var jmlAnak: Int = 0
    private var allFormValid: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        binding.toolbar.tvTitlePage.text = "Book"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initJumlah()
        setListener()
        setAccount()
        showFormTraveler()
    }

    private fun setListener() {
        binding.apply {
            btnPesan.setOnClickListener {
                bookTicket()
            }
        }
    }

    private fun initJumlah() {
        jmlDewasa = flightVM.getAdultPassenger().value!!
        jmlAnak =
            if (flightVM.getChildPassenger().value == null) 0 else flightVM.getChildPassenger().value!!
    }

    private fun setAccount() {
        val userVM = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        val token = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE)
            .getString("token", null)
        if (token != null) {
            userVM.getUser("Bearer $token").observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.tvUserName.text = it.data.firstname + " " + it.data.lastname
                    Glide.with(requireContext()).load(it.data.profileImage).into(binding.civProfile)
                }
            }
        }
    }

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    private fun showFormTraveler() {
        if (jmlDewasa > 0) {
            for (i in 0 until jmlDewasa) {
                val viewForm = LayoutInflater.from(context).inflate(R.layout.form_traveler, null)
                setMargin(viewForm)
                val formDewasaBinding = FormTravelerBinding.bind(viewForm)
                formDewasaBinding.labelTipeTraveler.text = "(Dewasa ${i + 1})"
                formDewasaBinding.etTglLahir.setOnClickListener {
                    setDatePicker(formDewasaBinding)
                }
                formDewasaBinding.etTglLahir.setOnFocusChangeListener { view, b ->
                    if (b) {
                        setDatePicker(formDewasaBinding)
                    }
                }
                setTitelTipe(formDewasaBinding)
                validateInputForm(formDewasaBinding, "dewasa")
                binding.formTravelerContainer.addView(viewForm)
            }
        }
        if (jmlAnak > 0) {
            for (i in 0 until jmlAnak) {
                val viewForm = LayoutInflater.from(context).inflate(R.layout.form_traveler, null)
                setMargin(viewForm)
                val formAnakBinding = FormTravelerBinding.bind(viewForm)
                formAnakBinding.apply {
                    labelTipeTraveler.text = "(Anak ${i + 1})"
                    containerTitel.visibility = View.GONE
                    containerTipeIdnt.visibility = View.GONE
                    containerNoIdentitas.visibility = View.GONE
                    etTglLahir.setOnClickListener {
                        setDatePicker(formAnakBinding)
                    }
                }
                validateInputForm(formAnakBinding, "anak")
                binding.formTravelerContainer.addView(viewForm)
            }
        }
    }

    private fun setTitelTipe(binding: FormTravelerBinding) {
        val adapterTitel = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.spinner_titel)
        )
        binding.etTitel.setText("Titel", false)
        binding.etTitel.setAdapter(adapterTitel)

        val adapterTipe = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.spinner_tipe)
        )
        binding.etTipe.setText("Tipe Identitas", false)
        binding.etTipe.setAdapter(adapterTipe)
    }

    private fun setDatePicker(binding: FormTravelerBinding) {
        val calendar = Calendar.getInstance()
        val datePicker =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateField(calendar.time, binding)
            }
        DatePickerDialog(
            requireActivity(), datePicker, calendar.get(Calendar.YEAR), calendar.get(
                Calendar.MONTH
            ), calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateField(date: Date, binding: FormTravelerBinding) {
        val myFormat = "dd MMM yy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.etTglLahir.setText(sdf.format(date))
    }

    private fun validateInputForm(bindForm: FormTravelerBinding, kategori: String) {
        binding.apply {
            etNamaLengkap.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (etNamaLengkap.text.isNullOrEmpty()) {
                        etNamaLengkap.error = "Nama tidak boleh kosong"
                    }
                }
            }
            etNoTelp.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (etNoTelp.text.isNullOrEmpty()) {
                        etNoTelp.error = "Nomor Telepon tidak boleh kosong"
                    }
                }
            }
            etEmail.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (etEmail.text.isNullOrEmpty()) {
                        etEmail.error = "Email tidak boleh kosong"
                    }
                }
            }
        }
        bindForm.apply {
            etTitel.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (etTitel.text.isNullOrEmpty()) {
                        etTitel.error = "Titel tidak boleh kosong"
                    }
                }
//            })
                etKewarganegaraan.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        if (etKewarganegaraan.text.isNullOrEmpty()) {
                            etKewarganegaraan.error = "Kewarganegaraan tidak boleh kosong"
                        }
                    }
                })
                if (kategori == "dewasa") {
                    etTipe.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                        if (!hasFocus) {
                            if (etTipe.text.isNullOrEmpty()) {
                                etTipe.error = "Tipe tidak boleh kosong"
                            }
                        }
                    })
                    etNoIdnt.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                        if (!hasFocus) {
                            if (etNoIdnt.text.isNullOrEmpty()) {
                                etNoIdnt.error = "No Identitas tidak boleh kosong"
                            }
                        }
                    })
                }
                etNamaDepanAdlt.setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        if (etNamaDepanAdlt.text.isNullOrEmpty()) {
                            etNamaDepanAdlt.error = "Nama Depan tidak boleh kosong"
                        }
                    }
                }
                etNamaBelakangAdlt.setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        if (etNamaBelakangAdlt.text.isNullOrEmpty()) {
                            etNamaBelakangAdlt.error = "Nama Belakang tidak boleh kosong"
                        }
                    }
                }
                etTglLahir.setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        if (etTglLahir.text.isNullOrEmpty()) {
                            etTglLahir.error = "Tanggal Lahir tidak boleh kosong"
                        }
                    }
                }
            })
        }
    }

    private fun setMargin(v: View) {
        v.layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT,
            ViewGroup.MarginLayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(24, 16, 24, 16)
        }
    }

    private fun getContactData(): DataKontak {
        val nama = binding.etNamaLengkap.text.toString()
        val noTelp = binding.etNoTelp.text.toString()
        val email = binding.etEmail.text.toString()
        return DataKontak(nama, noTelp, email)
    }

    private fun getTravelerData(): ArrayList<TravelerItem> {
        val arrDataPenumpang = ArrayList<TravelerItem>()
        binding.formTravelerContainer.children.forEach {
            val formTravelerBinding = FormTravelerBinding.bind(it)
            val title = formTravelerBinding.etTitel.text.toString()
            val name = formTravelerBinding.etNamaDepanAdlt.text.toString()
            val surname = formTravelerBinding.etNamaBelakangAdlt.text.toString()
            val datebirth = formTravelerBinding.etTglLahir.text.toString()
            val nationality = formTravelerBinding.etKewarganegaraan.text.toString()
            var idCard: String? = null
            var noKtp: String? = null
            var type: String
            if (formTravelerBinding.labelTipeTraveler.text.contains("Dewasa")) {
                idCard = formTravelerBinding.etNoIdnt.text.toString()
                noKtp = formTravelerBinding.etNoIdnt.text.toString()
                type = "adult"
            } else {
                type = "child"
            }
            val dataPnmp =
                TravelerItem(datebirth, nationality, surname, noKtp, name, idCard, type, title)
            arrDataPenumpang.add(dataPnmp)
            checkInput(formTravelerBinding)
        }
        return arrDataPenumpang
    }

    private fun checkInput(bindForm: FormTravelerBinding) {
        allFormValid =
            !(binding.etNamaLengkap.text.isNullOrEmpty() || binding.etNoTelp.text.isNullOrEmpty() || binding.etEmail.text.isNullOrEmpty() || bindForm.etTitel.text.isNullOrEmpty() || bindForm.etKewarganegaraan.text.isNullOrEmpty() || bindForm.etNamaDepanAdlt.text.isNullOrEmpty() || bindForm.etNamaBelakangAdlt.text.isNullOrEmpty() || bindForm.etTglLahir.text.isNullOrEmpty())
    }

    private fun bookTicket() {
        val dataKontak = getContactData()
        val dataTrav = getTravelerData()
        val userPrefs =
            requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE)
        val token = userPrefs.getString("token", null)
        if (token != null) {
            val idTicket = arguments?.getString("idTicket")
            val qtt = Quantity(adult = jmlDewasa, child = jmlAnak)
            val body = idTicket?.let { PostBookingBody(it, qtt, dataTrav) }
            if (allFormValid) {
                if (body != null) {
                    val action =
                        BookingFragmentDirections.actionBookingFragmentToReviewBookingFragment(
                            dataKontak,
                            body
                        )
                    findNavController().navigate(action)
                }
            } else {
                Toast.makeText(requireContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}