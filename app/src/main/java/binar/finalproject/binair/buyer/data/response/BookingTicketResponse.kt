package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class BookingTicketResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("status")
	val status: Int
) : java.io.Serializable

data class TravelerItem(

	@field:SerializedName("datebirth")
	val datebirth: String,

	@field:SerializedName("nationality")
	val nationality: String,

	@field:SerializedName("surname")
	val surname: String,

	@field:SerializedName("no_ktp")
	val noKtp: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id_card")
	val idCard: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("tittle")
	val tittle: String
)

data class DataItem(

	@field:SerializedName("ticketsId")
	val ticketsId: String,

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("quantity")
	val quantity: Quantity,

	@field:SerializedName("traveler")
	val traveler: List<TravelerItem>,

	@field:SerializedName("amounts")
	val amounts: Int,

	@field:SerializedName("usersId")
	val usersId: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("payment_method")
	val paymentMethod: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class Quantity(

	@field:SerializedName("adult")
	val adult: Int,

	@field:SerializedName("child")
	val child: Int
)
