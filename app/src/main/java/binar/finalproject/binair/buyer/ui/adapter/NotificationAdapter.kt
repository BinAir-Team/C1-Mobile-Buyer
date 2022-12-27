package binar.finalproject.binair.buyer.ui.adapter
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.data.response.DataNotif
import binar.finalproject.binair.buyer.databinding.ItemNotificationBinding

@RequiresApi(Build.VERSION_CODES.O)
class NotificationAdapter(private val listNotif : List<DataNotif>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    var onupdate: ((DataNotif) -> Unit)? = null

    class  ViewHolder(val binding : ItemNotificationBinding, private var onupdate : (((DataNotif) -> Unit)?)) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : DataNotif){
            binding.itemnotif = item
            val pattern = Regex("""\d{1,2}\ \w+\ \d{4}""")
            val res = item.message?.let { pattern.find(it, 0) }
            binding.notifDate.text = res?.value
            if(item.isRead == true){
                binding.lvNotif.setBackgroundColor(Color.parseColor("#EBEEF1"))
                binding.belumdibaca.visibility = View.GONE
                binding.sudahdibaca.visibility = View.VISIBLE
            }
            binding.belumdibaca.setOnClickListener {
                onupdate?.invoke(item)
                if(item.isRead == true){
                    binding.lvNotif.setBackgroundColor(Color.parseColor("#EBEEF1"))
                    binding.belumdibaca.visibility = View.GONE
                    binding.sudahdibaca.visibility = View.VISIBLE
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, onupdate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNotif[position])
    }

    override fun getItemCount(): Int = listNotif.size

}