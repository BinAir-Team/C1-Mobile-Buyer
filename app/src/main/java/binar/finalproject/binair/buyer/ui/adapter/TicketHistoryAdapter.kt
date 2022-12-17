package binar.finalproject.binair.buyer.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.data.response.TransItem
import binar.finalproject.binair.buyer.databinding.ItemHistoryBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class TicketHistoryAdapter(private val listTicket : List<TransItem>) : RecyclerView.Adapter<TicketHistoryAdapter.ViewHolder>() {
    var onClick: ((TransItem) -> Unit)? = null
    class ViewHolder(private val binding : ItemHistoryBinding, private var onClick : (((TransItem) -> Unit)?)): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: TransItem) {
            binding.apply {
                if(item.status.split(" ")[0] == "PENDING"){
                    tvStatusPayment.text = "TERTUNDA"
                }else{
                    tvStatusPayment.text = "LUNAS"
                    tvStatusPayment.setTextColor(Color.BLACK)
                    tvStatusPayment.setBackgroundColor(Color.GREEN)
                }
                tvDate.text = formatDate(item.ticket.dateStart)
                tvKodePemesanan.text = item.id.split("-").get(0)
                tvDepartureTime.text = item.ticket.departureTime
                tvDepartureCity.text = item.ticket.from
                tvArrivalTime.text = item.ticket.arrivalTime
                tvArrivalCity.text = item.ticket.to

                cvHistory.setOnClickListener {
                    onClick?.invoke(item)
                }
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
        val v = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v,onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTicket[position])
    }

    override fun getItemCount(): Int = listTicket.size
}