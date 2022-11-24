package binar.finalproject.binair.buyer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.databinding.ItemPromoBinding

class HomePromoAdapter(val listPromo : ArrayList<String>) : RecyclerView.Adapter<HomePromoAdapter.ViewHolder>(){
    class ViewHolder(val binding: ItemPromoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(promo : String){
            binding.titlePromo.text = promo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPromo[position])
    }

    override fun getItemCount(): Int = listPromo.size

}