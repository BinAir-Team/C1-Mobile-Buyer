package binar.finalproject.binair.buyer.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import binar.finalproject.binair.buyer.R


class PaymentMethodAdapter(
    private val ctx: Context, resource: Int, private val contentArray: Array<String>,
    private val imageArray: Array<Int?>
) : ArrayAdapter<String?>(
    ctx, R.layout.item_payment_method, R.id.tvNamePayment,
    contentArray
) {
    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    @SuppressLint("InflateParams")
    override fun getDropDownView(position: Int, paramConvertView: View?, parent: ViewGroup): View {
        var convertView = paramConvertView
        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.item_payment_method, null)
        }
        val textView = convertView!!.findViewById<View>(R.id.tvNamePayment) as TextView
        val imageView: ImageView = convertView.findViewById<View>(R.id.ivLogoPayment) as ImageView
        if(position == 0){
            textView.text = contentArray[position]
            textView.setTextColor(Color.GRAY)
            imageView.visibility = View.GONE
        }else{
            textView.text = contentArray[position]
            imageView.setImageResource(imageArray[position]!!)
        }
        return convertView
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, paramConvertView: View?, parent: ViewGroup): View {
        var convertView = paramConvertView
        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.item_payment_method, null)
        }
        val textView = convertView!!.findViewById<View>(R.id.tvNamePayment) as TextView
        val imageView: ImageView = convertView.findViewById<View>(R.id.ivLogoPayment) as ImageView
        if(position == 0){
            textView.text = contentArray[position]
            textView.setTextColor(Color.GRAY)
            imageView.visibility = View.GONE
        }else{
            textView.text = contentArray[position]
            imageView.setImageResource(imageArray[position]!!)
        }
        return convertView
    }
}