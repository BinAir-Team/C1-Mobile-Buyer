package binar.finalproject.binair.buyer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.data.response.DataPromo
import binar.finalproject.binair.buyer.databinding.ItemPromoBinding
import com.bumptech.glide.Glide

class HomePromoAdapter(val listPromo : List<DataPromo>) : RecyclerView.Adapter<HomePromoAdapter.ViewHolder>(){
    var onClick: ((DataPromo) -> Unit)? = null
    class ViewHolder(
        private val binding: ItemPromoBinding,
        private val onClick : (((DataPromo) -> Unit)?)
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(promo : DataPromo){
            binding.promo = promo
            Glide.with(itemView)
                .load(promo.promoImage)
                .into(binding.ivPromo)

            binding.cvPromo.setOnClickListener{
                onClick?.invoke(promo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPromo[position])
    }

    override fun getItemCount(): Int = listPromo.size

}