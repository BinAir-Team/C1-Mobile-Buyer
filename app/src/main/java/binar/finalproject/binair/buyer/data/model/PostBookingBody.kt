package binar.finalproject.binair.buyer.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PostBookingBody(

	@field:SerializedName("ticketsId")
	val ticketsId: String,

	@field:SerializedName("quantity")
	val quantity: Quantity,

	@field:SerializedName("traveler")
	val traveler: List<TravelerItem>,
) : java.io.Serializable

@Parcelize
data class TravelerItem(

	@field:SerializedName("datebirth")
	val datebirth: String,

	@field:SerializedName("nationality")
	val nationality: String,

	@field:SerializedName("surname")
	val surname: String,

	@field:SerializedName("no_ktp")
	val noKtp: String?,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id_card")
	val idCard: String?,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("tittle")
	val tittle: String
) : Parcelable

data class Quantity(

	@field:SerializedName("adult")
	val adult: Int,

	@field:SerializedName("child")
	val child: Int
)
