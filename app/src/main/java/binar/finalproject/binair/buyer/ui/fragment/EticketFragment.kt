package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.binair.buyer.data.response.BookingTicketResponse
import binar.finalproject.binair.buyer.data.response.TransItem
import binar.finalproject.binair.buyer.databinding.FragmentEticketBinding
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetTextI18n")
class EticketFragment : Fragment() {
    private lateinit var binding : FragmentEticketBinding
    private lateinit var flightVM : FlightViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEticketBinding.inflate(inflater, container, false)
        flightVM = ViewModelProvider(this).get(FlightViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showData()
    }

    @SuppressLint("SetTextI18n")
    private fun showData(){
        try {
            val args = arguments?.getSerializable("dataBooking") as BookingTicketResponse
            setData(args.data[0].ticketsId)
            createQR(args.data[0].id)
        }catch (e : Exception){
            e.printStackTrace()
            val argsTrans = arguments?.getSerializable("itemTrans") as TransItem
            setData(argsTrans.ticketsId)
            createQR(argsTrans.id)
        }

    }

    private fun setData(idTicket : String){
        flightVM.getTicketById(idTicket).observe(viewLifecycleOwner){
            if(it != null){
                val alphabets = ('A'..'Z')
                binding.ticket = it.data
                binding.tvTanggal.text = formatDate(it.data.dateStart)
                if(it.data.dateEnd != null){
                    binding.tvTglPulang.text = formatDate(it.data.dateEnd?:"")
                }else{
                    binding.labelTglPulang.visibility = View.GONE
                    binding.tvTglPulang.visibility = View.GONE
                }
                binding.tvFlightNum.text = "IN${Random.nextInt(100, 999)}"
                binding.tvKursi.text = Random.nextInt(1,20).toString() + alphabets.random()
                binding.tvGate.text = Random.nextInt(1,20).toString()
            }
        }
    }

    private fun createQR(idTrans : String){
        val size = 512 //pixels
        //        val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 }
        val bits = QRCodeWriter().encode(idTrans, BarcodeFormat.QR_CODE, size, size)
        val bitmp =  Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
        binding.ivQr.setImageBitmap(bitmp)
    }

    private fun formatDate(date : String) : String {
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = LocalDate.parse(date, formatter)
            val formatter2 = DateTimeFormatter.ofPattern("dd MMM yyyy")
            return localDate.format(formatter2)
        }catch (e : Exception){
            return date
        }
    }
}