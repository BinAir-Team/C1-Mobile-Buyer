package binar.finalproject.binair.buyer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.data.local.DataWishList
import binar.finalproject.binair.buyer.databinding.ItemWishlistBinding

class WishListAdapter (private var onClick : WishListAdapter.NotesInterface) : RecyclerView.Adapter<WishListAdapter.ViewHolder>(){

    private var diffCallback = object : DiffUtil.ItemCallback<DataWishList>(){

        override fun areItemsTheSame(oldItem: DataWishList, newItem: DataWishList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataWishList, newItem: DataWishList): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }
    inner class ViewHolder (private val binding: ItemWishlistBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(notes: DataWishList){
            binding.apply {
                dataWishlist = notes
            }
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    interface NotesInterface {
        fun editNote(notes: DataWishList)
        fun deleteNote(notes: DataWishList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    fun setData(data : List<DataWishList>){
        differ.submitList(data)
    }
}