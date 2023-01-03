package binar.finalproject.binair.buyer.data.response

import com.google.gson.annotations.SerializedName

data class ForgetPasswordResponse(

    @field:SerializedName("data")
    val data: DataForgetPass?,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class DataForgetPass(
	val any: Any? = null
)
