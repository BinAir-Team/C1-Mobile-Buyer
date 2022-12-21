package binar.finalproject.binair.buyer.data.response


import com.google.gson.annotations.SerializedName

data class UpdateNotificationResponse(
    @SerializedName("data")
    val `data`: List<DataUpdateNotification?>?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("status")
    val status: Int?
)

data class DataUpdateNotification(
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
    @SerializedName("usersId")
    val usersId: String?
)