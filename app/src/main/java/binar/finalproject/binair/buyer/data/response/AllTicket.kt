package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class AllTicket(

	@field:SerializedName("AllTicket")
	val allTicket: List<TicketItem?>? = null
)

data class TicketItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("hours")
	val hours: String? = null,

	@field:SerializedName("child_price")
	val childPrice: Int? = null,

	@field:SerializedName("adult_price")
	val adultPrice: Int? = null,

	@field:SerializedName("available")
	val available: Boolean? = null,

	@field:SerializedName("curr_stock")
	val currStock: Int? = null,

	@field:SerializedName("from")
	val from: String? = null,

	@field:SerializedName("to")
	val to: String? = null,

	@field:SerializedName("init_stock")
	val initStock: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)
