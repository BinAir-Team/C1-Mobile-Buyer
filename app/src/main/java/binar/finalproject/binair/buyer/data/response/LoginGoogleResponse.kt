package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class LoginGoogleResponse(

	@field:SerializedName("data")
	val data: DataLoginGoogle,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataLoginGoogle(

	@field:SerializedName("firstname")
	val firstname: String,

	@field:SerializedName("profile_image")
	val profileImage: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("lastname")
	val lastname: String
)
