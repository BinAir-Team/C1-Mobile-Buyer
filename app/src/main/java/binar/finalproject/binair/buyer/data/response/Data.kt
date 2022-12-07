package binar.finalproject.binair.buyer.data.response


import com.google.gson.annotations.SerializedName

data class DataPromo(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("discount")
    val discount: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("promo_code")
    val promoCode: String,
    @SerializedName("promo_image")
    val promoImage: String,
    @SerializedName("terms")
    val terms: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)