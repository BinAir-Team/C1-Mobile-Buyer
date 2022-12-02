package binar.finalproject.binair.buyer.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataPenumpang(
    var title : String,
    var name : String,
    var surname : String,
    var datebirth : String,
    var nationality : String,
    var id_card : String?,
    var no_ktp : String?,
    var type : String
) : Parcelable
