package binar.finalproject.binair.buyer.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.Constant
import binar.finalproject.binair.buyer.data.formatRupiah
import binar.finalproject.binair.buyer.data.makeNotification
import binar.finalproject.binair.buyer.data.response.BookingTicketResponse
import binar.finalproject.binair.buyer.data.response.TransItem
import binar.finalproject.binair.buyer.data.response.TravelerItem
import binar.finalproject.binair.buyer.databinding.FragmentPaymentBinding
import binar.finalproject.binair.buyer.socketio.SocketHandler
import binar.finalproject.binair.buyer.ui.adapter.PaymentMethodAdapter
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class PaymentFragment : Fragment() {
    private lateinit var binding : FragmentPaymentBinding
    private lateinit var flightVM : FlightViewModel
    private var image : MultipartBody.Part? = null
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
            image = MultipartBody.Part.createFormData("bukti_bayar", tempFile.name, reqBody)
        }
    private val REQUEST_CODE_PERMISSION = 100

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(requireActivity()).get(FlightViewModel::class.java)
        binding.toolbar.tvTitlePage.text = "Pembayaran"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSocketIO()
        setListener()
        getSetData()
        initDataSpinner()
    }

    private fun initSocketIO(){
        SocketHandler.setSocket()
        val mSocket = SocketHandler.getSocket()
        mSocket.connect()

        mSocket.on("notify-update"){
            Log.e("SocketHandler", it[0].toString())
            if(it[0] != null){
                makeNotification("Binair",it[0].toString(), requireContext())
            }
        }
    }

    private fun setListener(){
        binding.ivPaymentProof.setOnClickListener {
            checkingPermissions()
        }
        binding.btnBayar.setOnClickListener {
            if(image != null){
                updatePaymentStatus()
            }else{
                Toast.makeText(requireContext(), "Silahkan upload bukti pembayaran", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getSetData(){
        try {
            val args = arguments?.getSerializable("dataBooking") as BookingTicketResponse
            val dataBook = args.data[0]
            setDataToView(dataBook.ticketsId,dataBook.id,dataBook.traveler,dataBook.amounts)
        }catch (e : Exception){
            e.printStackTrace()
            val dataTrans = arguments?.getSerializable("itemTrans") as TransItem
            setDataToView(dataTrans.ticketsId,dataTrans.id,dataTrans.traveler,dataTrans.amounts)
        }
    }

    private fun setDataToView(idTicket : String, idBooking : String, dataTrav : List<TravelerItem>, amount : Int){
        binding.apply {
            flightVM.getTicketById(idTicket).observe(viewLifecycleOwner) {
                if (it != null) {
                    tvFlightDateGo.text = formatDate(it.data.dateStart)
                    tvKotaAsal.text = it.data.from
                    tvKotaTujuan.text = it.data.to
                    if (it.data.type == "oneway") {
                        tvFlightDateBack.visibility = View.GONE
                        tvKotaAsalKembali.visibility = View.GONE
                        tvKotaTujuanKembali.visibility = View.GONE
                    }else{
                        tvFlightDateBack.text = it.data.dateEnd?.let { it1 -> formatDate(it1) }
                        tvKotaAsalKembali.text = it.data.to
                        tvKotaTujuanKembali.text = it.data.from
                    }
                }
            }
            tvIdBooking.text = idBooking
            var passenger = ""
            var passengerType = ""
            for (i in dataTrav) {
                val formatedType = if (i.type.equals("adult")) "Dewasa" else "Anak"
                passenger += "${i.name}\n"
                passengerType += "${formatedType}\n"
            }
            tvName.text = passenger
            tvType.text = passengerType
            tvTotalPrice.text = formatRupiah(amount)
        }
    }

    private fun updatePaymentStatus(){
        val token = requireActivity().getSharedPreferences(Constant.dataUser, Context.MODE_PRIVATE).getString("token", null)
        val id = binding.tvIdBooking.text.toString()
        val method = binding.spPaymentMethod.selectedItem.toString().toRequestBody("text/plain".toMediaType())

        flightVM.updatePayment("Bearer $token",id,image!!,method).observe(viewLifecycleOwner){
            if (it != null) {
                if(it.status == 200){
//                    Toast.makeText(requireContext(), "Pembayaran berhasil", Toast.LENGTH_SHORT).show()
                    makeNotification("Pembayaran","Pembayaran Berhasil Dilakukan",requireContext())
                    val args = arguments?.getSerializable("dataBooking") as BookingTicketResponse
                    for(trav in args.data[0].traveler){
                        if (trav.noKtp == null){
                            trav.noKtp = ""
                            trav.idCard = ""
                        }
                        if(trav.tittle == null){
                            trav.tittle = ""
                        }
                    }
                    try {
                        val act = PaymentFragmentDirections.actionPaymentFragmentToEticketFragment(args,null)
                        findNavController().navigate(act)
                    }catch (e : Exception){
                        val argsTrans = arguments?.getSerializable("itemTrans") as TransItem
                        for(trav in argsTrans.traveler){
                            if (trav.noKtp == null){
                                trav.noKtp = ""
                                trav.idCard = ""
                            }
                            if(trav.tittle == null){
                                trav.tittle = ""
                            }
                        }
                        val act = PaymentFragmentDirections.actionPaymentFragmentToEticketFragment(null,argsTrans)
                        findNavController().navigate(act)
                    }
                }
            }else{
                Toast.makeText(requireContext(), "Pembayaran gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatDate(date : String) : String {
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = LocalDate.parse(date, formatter)
            val formatter2 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale("id","ID"))
            return localDate.format(formatter2)
        }catch (e : Exception){
            return date
        }
    }

    private fun initDataSpinner() {
        val arrName = arrayOf("Pilih pembayaran...","Bank BCA", "Bank Mandiri", "Bank BNI", "Bank BRI","Go-Pay","OVO","ShopeePay")
        val arrPhoto = arrayOf(null,R.drawable.logo_bca, R.drawable.logo_mandiri, R.drawable.logo_bni, R.drawable.logo_bri,R.drawable.logo_gopay,R.drawable.logo_ovo,R.drawable.logo_shopee_pay)
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