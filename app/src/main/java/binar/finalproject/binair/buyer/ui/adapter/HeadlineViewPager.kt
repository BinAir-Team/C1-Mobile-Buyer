package binar.finalproject.binair.buyer.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.model.News
import binar.finalproject.binair.buyer.databinding.HeadlineNewsBinding
import binar.finalproject.binair.buyer.databinding.ItemPromoBinding
import com.bumptech.glide.Glide

class HeadlineViewPager(var headlineNewsList: ArrayList<News>): RecyclerView.Adapter<HeadlineViewPager.ViewHolder>() {

    class ViewHolder(val binding: HeadlineNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(img : Int){

//            binding.tvTitleHeadline.text = title
//            binding.tvDateHeadline.text = date
            binding.ivHeadline.setImageResource(img)
//            binding.tvEditorHeadline.text = editor


    }
}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = HeadlineNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(headlineNewsList[position].imgUrl)
    }

    override fun getItemCount(): Int {
        return headlineNewsList.size
    }

    fun setHeadlineNewsData(headlineNewsList: ArrayList<News>){
        this.headlineNewsList = headlineNewsList
    }
}