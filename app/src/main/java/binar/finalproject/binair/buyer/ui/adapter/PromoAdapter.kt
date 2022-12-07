package binar.finalproject.binair.buyer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.data.response.DataPromo
import binar.finalproject.binair.buyer.databinding.ItemPromoPageBinding


class PromoAdapter(private val listPromo : List<DataPromo>) : RecyclerView.Adapter<PromoAdapter.ViewHolder>(){

    var onClick: ((DataPromo) -> Unit)? =null
    class ViewHolder(private val binding : ItemPromoPageBinding, private var onClick : (((DataPromo) -> Unit)?) ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataPromo){
            binding.promo = item
            binding.cvPromo.setOnClickListener {
                onClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val p = ItemPromoPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(p,onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPromo[position])
    }

    override fun getItemCount(): Int = listPromo.size

}