package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("data")
	val data: Data?,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("firstname")
	val firstname: String,

	@field:SerializedName("lastname")
	val lastname: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("profile_image")
	val profileImage: String
)
