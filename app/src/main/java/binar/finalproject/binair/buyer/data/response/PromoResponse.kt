package binar.finalproject.binair.buyer.data.response


import com.google.gson.annotations.SerializedName

data class PromoResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)