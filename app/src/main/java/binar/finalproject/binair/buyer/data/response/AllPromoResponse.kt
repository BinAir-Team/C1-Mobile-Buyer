package binar.finalproject.binair.buyer.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AllPromoResponse(
    @SerializedName("data")
    val `data`: List<DataPromo>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?
)

@Parcelize
data class DataPromo(
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("desc")
    val desc: String?,
    @SerializedName("discount")
    val discount: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("promo_code")
    val promoCode: String?,
    @SerializedName("promo_image")
    val promoImage: String?,
    @SerializedName("terms")
    val terms: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
) : Parcelable