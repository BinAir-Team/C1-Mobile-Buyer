package binar.finalproject.binair.buyer.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.formatRupiah
import binar.finalproject.binair.buyer.data.response.BookingTicketResponse
import binar.finalproject.binair.buyer.data.response.GetTicketByIdResponse
import binar.finalproject.binair.buyer.databinding.FragmentPaymentRoundedTripBinding
import binar.finalproject.binair.buyer.ui.adapter.PaymentMethodAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PaymentRoundedTripFragment : Fragment() {
    private lateinit var binding : FragmentPaymentRoundedTripBinding
    private lateinit var flightVM : FlightViewModel
    private lateinit var image : MultipartBody.Part
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.ivPaymentProof.setImageURI(result)
            val contentResolver = activity?.contentResolver
            val type = contentResolver?.getType(result!!)

            val tempFile = File.createTempFile("image", ".jpg",null)
            val inputStream = contentResolver?.openInputStream(result!!)
            tempFile.outputStream().use {
                inputStream?.copyTo(it)
            }
            val reqBody : RequestBody = tempFile.asRequestBody(type!!.toMediaType())
            image = MultipartBody.Part.createFormData("payment_proof", tempFile.name, reqBody)
        }
    private val REQUEST_CODE_PERMISSION = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentRoundedTripBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        getSetData()
        initDataSpinner()
    }

    private fun setListener(){
        binding.ivPaymentProof.setOnClickListener {
            checkingPermissions()
        }
    }

    private fun getSetData(){
        val args = arguments?.getSerializable("dataBooking") as BookingTicketResponse
        val dataBook = args.data[0]
        var ticket : GetTicketByIdResponse? = null
        flightVM.getTicketById(dataBook.ticketsId).observe(viewLifecycleOwner){
            ticket = it
        }
        binding.apply {
            tvIdBooking.text = dataBook.id
            tvFlightDate.text = SimpleDateFormat("EE, dd MMMM yyyy", Locale.getDefault()).format(dataBook.date)
            tvKotaAsal.text = ticket?.data?.from ?: ""
            tvKotaTujuan.text = ticket?.data?.to ?: ""
            var passenger = ""
            var passengerType = ""
            for (i in dataBook.traveler){
                passenger += "${i.name}\n"
                passengerType += "${i.type}\n"
            }
            tvTotalPrice.text = formatRupiah(dataBook.amounts)
        }
    }

    private fun initDataSpinner() {
        val arrName = arrayOf("Pilih pembayaran...","Bank BCA", "Bank Mandiri", "Bank BNI", "Bank BRI","Go-Pay","OVO","ShopeePay")
        val arrPhoto = arrayOf(null,
            R.drawable.logo_bca, R.drawable.logo_mandiri, R.drawable.logo_bni, R.drawable.logo_bri,
            R.drawable.logo_gopay,
            R.drawable.logo_ovo,
            R.drawable.logo_shopee_pay)
        binding.spPaymentMethod.adapter = PaymentMethodAdapter(requireContext(), R.layout.item_payment_method, arrName, arrPhoto)
    }

    private fun checkingPermissions() {
        if (isGranted(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,)
        ){
            openGallery()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

}