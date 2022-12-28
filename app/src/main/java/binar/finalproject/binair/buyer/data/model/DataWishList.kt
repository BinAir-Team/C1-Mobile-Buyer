package binar.finalproject.binair.buyer.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@kotlinx.parcelize.Parcelize
@Entity
data class DataWishList(
    @PrimaryKey(autoGenerate = true)
    val idWishlist : Int,
    val dateStart: String,
    var arrivalTime: String? = null,
    var airportTo: String? = null,
    var childPrice: Int,
    var adultPrice: Int,
    val airportFrom: String,
    val from: String,
    var dateEnd: String? = null,
    val id: String,
    val to: String,
    val type: String,
    val departureTime: String,
    val user : String
):Parcelable

@kotlinx.parcelize.Parcelize
data class TrueFalseWishlist(
    val isWishlist : Boolean
) : Parcelable