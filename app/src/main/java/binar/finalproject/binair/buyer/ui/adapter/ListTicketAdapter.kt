package binar.finalproject.binair.buyer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.data.response.TicketItem
import binar.finalproject.binair.buyer.databinding.ItemTicketBinding

class ListTicketAdapter(private val listTicket : List<TicketItem>) : RecyclerView.Adapter<ListTicketAdapter.ViewHolder>() {
    class ViewHolder(private val binding : ItemTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TicketItem) {
            binding.ticket = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTicket[position])
    }

    override fun getItemCount(): Int = listTicket.size
}