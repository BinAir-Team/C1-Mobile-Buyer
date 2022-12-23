package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class GetUserTransResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: DataTrans,

	@field:SerializedName("status")
	val status: Int? = null
)

data class TransItem(

	@field:SerializedName("ticketsId")
	val ticketsId: String,

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("quantity")
	val quantity: Quantity,

	@field:SerializedName("traveler")
	val traveler: List<TravelerItem>,

	@field:SerializedName("ticket")
	val ticket: Ticket,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("amounts")
	val amounts: Int,

	@field:SerializedName("usersId")
	val usersId: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("payment_method")
	val paymentMethod: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : java.io.Serializable

data class Ticket(

	@field:SerializedName("date_start")
	val dateStart: String,

	@field:SerializedName("arrival_time")
	var arrivalTime: String? = null,

	@field:SerializedName("airport_to")
	var airportTo: String? = null,

	@field:SerializedName("child_price")
	val childPrice: Int,

	@field:SerializedName("adult_price")
	val adultPrice: Int,

	@field:SerializedName("airport_from")
	val airportFrom: String,

	@field:SerializedName("from")
	val from: String,

	@field:SerializedName("date_end")
	var dateEnd: String? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("to")
	val to: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("departure_time")
	val departureTime: String
): java.io.Serializable

data class User(

	@field:SerializedName("firstname")
	val firstname: String,

	@field:SerializedName("profile_image")
	val profileImage: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("verified")
	val verified: Boolean,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lastname")
	val lastname: String
): java.io.Serializable

data class DataTrans(

	@field:SerializedName("totalItems")
	val totalItems: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("transactions")
	val transactions: List<TransItem>? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null
)
