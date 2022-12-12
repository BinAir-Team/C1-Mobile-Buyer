package binar.finalproject.binair.buyer.data

import java.text.NumberFormat
import java.util.*

fun formatRupiah(value: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatter.format(value)
}