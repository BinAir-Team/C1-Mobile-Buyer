package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("data")
	val data: DataUser,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
