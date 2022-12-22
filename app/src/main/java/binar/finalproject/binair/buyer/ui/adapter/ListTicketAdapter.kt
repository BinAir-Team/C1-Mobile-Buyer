package binar.finalproject.binair.buyer.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.data.formatRupiah
import binar.finalproject.binair.buyer.data.response.TicketItem
import binar.finalproject.binair.buyer.databinding.ItemTicketBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ListTicketAdapter(private val listTicket : List<TicketItem>) : RecyclerView.Adapter<ListTicketAdapter.ViewHolder>() {
    var onClick: ((TicketItem) -> Unit)? = null
    var onClickWishlist : ((TicketItem) -> Unit)? = null
    class ViewHolder(val binding : ItemTicketBinding, private var onClick : (((TicketItem) -> Unit)?), private var onClickWishlist : (((TicketItem) -> Unit)?)): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TicketItem) {
            try{
                item.dateStart = formatDate(item.dateStart)
                item.dateEnd = formatDate(item.dateEnd)
            }catch (e : Exception){
                e.printStackTrace()
            }
            val dpTime = SimpleDateFormat("HH:mm").parse(item.departureTime)
            val arTime = SimpleDateFormat("HH:mm").parse(item.arrivalTime)
            val difference = arTime.time - dpTime.time
            val days = (difference / (1000 * 60 * 60 * 24)).toInt()
            val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60))
            val min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours) / (1000 * 60)
            binding.ticket = item
            binding.tvHours.text = "${hours}j ${min}menit"
            binding.tvHargaDewasa.text = formatRupiah(item.adultPrice)
            binding.tvHargaAnak.text = formatRupiah(item.childPrice)
            if(item.type == "oneway" || item.type == "Sekali Jalan") {
                binding.labelTglPulang.visibility = View.GONE
                binding.tvTglPulang.visibility = View.GONE
            }
            binding.cvTicket.setOnClickListener {
                onClick?.invoke(item)
            }
        }

        fun formatDate(date : String) : String {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v,onClick,onClickWishlist)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTicket[position])
    }

    override fun getItemCount(): Int = listTicket.size
}