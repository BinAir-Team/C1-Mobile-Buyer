package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("user")
	val user: List<Int>
)
