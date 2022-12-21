package binar.finalproject.binair.buyer.data.response


import com.google.gson.annotations.SerializedName

data class GetAllNotif(
    @SerializedName("data")
    val `data`: List<DataNotif>,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Int?
)

data class DataNotif(
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("isRead")
    val isRead: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("user")
    val user: UserNotif?,
    @SerializedName("usersId")
    val usersId: String?
)

data class UserNotif(
    @SerializedName("firstname")
    val firstname: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("verified")
    val verified: Boolean?
)