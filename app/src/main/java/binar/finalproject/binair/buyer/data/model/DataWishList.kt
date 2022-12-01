package binar.finalproject.binair.buyer.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class DataWishList(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var flightNumber: String,
    var timeDepart : String,
    var destination : String,
    var progress : String
) : Parcelable
