package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class UserNotificationResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("data")
	val data: List<NotifItem>,

	@field:SerializedName("status")
	val status: Int
)

data class NotifItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("usersId")
	val usersId: String,

	@field:SerializedName("isRead")
	val isRead: Boolean,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: UserNotif,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class UserNotif(

	@field:SerializedName("firstname")
	val firstname: String,

	@field:SerializedName("verified")
	val verified: Boolean,

	@field:SerializedName("id")
	val id: String
)
