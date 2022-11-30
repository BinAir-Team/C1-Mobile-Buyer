package binar.finalproject.binair.buyer.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataKontak(
    val namaLengkap : String,
    val nomorTelpon : String,
    val email : String
) : Parcelable
