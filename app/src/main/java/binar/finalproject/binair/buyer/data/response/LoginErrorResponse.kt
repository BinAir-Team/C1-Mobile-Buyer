package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class LoginErrorResponse(

	@field:SerializedName("data")
	val data: DataLoginError,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataLoginError(
	val any: Any? = null
)
