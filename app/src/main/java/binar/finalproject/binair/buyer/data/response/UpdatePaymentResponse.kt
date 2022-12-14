package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class UpdatePaymentResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("data")
	val data: List<Int>,

	@field:SerializedName("status")
	val status: Int
)
