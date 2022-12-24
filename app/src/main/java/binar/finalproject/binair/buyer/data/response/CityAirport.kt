package binar.finalproject.binair.buyer.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class CityAirport(
    @PrimaryKey
    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("airport")
    val airport: String
)
