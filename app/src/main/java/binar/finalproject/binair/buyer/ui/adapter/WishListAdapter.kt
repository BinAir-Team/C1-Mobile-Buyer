package binar.finalproject.binair.buyer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.databinding.ItemWishlistBinding

class WishListAdapter : RecyclerView.Adapter<WishListAdapter.ViewHolder>(){

    var onClick: ((DataWishList) -> Unit)? = null
    inner class ViewHolder (
        private val binding: ItemWishlistBinding,
        private var onClick : (((DataWishList) -> Unit)?))
        : RecyclerView.ViewHolder(binding.root){
        fun bind(data: DataWishList){
            binding.apply {
                dataWishlist = data
            }
            binding.cvWishlist.setOnClickListener {
                onClick?.invoke(data)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    fun setData(data : List<DataWishList>){
        differ.submitList(data)
    }

    private var diffCallback = object : DiffUtil.ItemCallback<DataWishList>(){

        override fun areItemsTheSame(oldItem: DataWishList, newItem: DataWishList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataWishList, newItem: DataWishList): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
}