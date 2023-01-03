package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class UpdateUserPasswordResponse(

    @field:SerializedName("data")
    val data: DataUpdatePass?,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class DataUpdatePass(
	val any: Any? = null
)
