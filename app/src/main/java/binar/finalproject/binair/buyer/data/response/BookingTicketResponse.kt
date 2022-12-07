package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class BookingTicketResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("data")
	val data: List<DataBookingItem>,

	@field:SerializedName("status")
	val status: Int
)

data class DataBookingItem(

	@field:SerializedName("ticketsId")
	val ticketsId: String,

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("quantity")
	val quantity: Quantity,

	@field:SerializedName("traveler")
	val traveler: Traveler,

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
