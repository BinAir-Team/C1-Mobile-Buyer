package binar.finalproject.binair.buyer.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import binar.finalproject.binair.buyer.data.response.BookingTicketResponse
import binar.finalproject.binair.buyer.databinding.FragmentEticketBinding
import binar.finalproject.binair.buyer.viewmodel.FlightViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
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
        val args = arguments?.getSerializable("dataBooking") as BookingTicketResponse
        flightVM.getTicketById(args.data[0].ticketsId).observe(viewLifecycleOwner){
            if(it != null){
                val alphabets = ('A'..'Z')
                binding.ticket = it.data
                binding.tvFlightNum.text = "IN${Random.nextInt(100, 999)}"
                binding.tvKursi.text = Random.nextInt(20).toString() + alphabets.random()
                binding.tvGate.text = Random.nextInt(20).toString()
            }
        }
        createQR(args.data[0].id)
    }

    private fun createQR(idTrans : String){
        val size = 512 //pixels
        val qrCodeContent = idTrans
//        val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 }
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size)
        val bitmp =  Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
        binding.ivQr.setImageBitmap(bitmp)
    }
}