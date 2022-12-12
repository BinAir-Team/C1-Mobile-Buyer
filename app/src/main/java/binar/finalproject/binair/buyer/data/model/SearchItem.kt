package binar.finalproject.binair.buyer.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchItem(
    val cityFrom : String,
    val airportFrom : String,
    val cityTo : String,
    val airportTo : String,
    val dateGo : String,
    val dateBack : String?,
    val type : String,
    val totalPassenger : Int
) : Parcelable
