package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class RegisterUserResponse(

	@field:SerializedName("data")
	val data: DataUser?,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataUser(

	@field:SerializedName("firstname")
	val firstname: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("lastname")
	val lastname: String
)
