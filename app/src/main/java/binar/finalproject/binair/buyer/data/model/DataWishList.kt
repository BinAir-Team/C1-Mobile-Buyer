package binar.finalproject.binair.buyer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataWishList(
    @PrimaryKey(autoGenerate = true)
    val idWishlist : Int,
    val dateStart: String,
    var arrivalTime: String? = null,
    var airportTo: String? = null,
    val childPrice: Int,
    val adultPrice: Int,
    val airportFrom: String,
    val from: String,
    var dateEnd: String? = null,
    val id: String,
    val to: String,
    val type: String,
    val departureTime: String
): java.io.Serializable
