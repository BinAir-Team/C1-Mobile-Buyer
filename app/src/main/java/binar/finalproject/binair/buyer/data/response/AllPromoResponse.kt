package binar.finalproject.binair.buyer.data.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AllPromoResponse(

	@field:SerializedName("data")
	val data: DataPagePromo,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

@Parcelize
@Entity
data class DataPromo(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("promo_image")
	val promoImage: String,

	@field:SerializedName("terms")
	val terms: String,

	@field:SerializedName("expire")
	val expire: String,

	@field:SerializedName("discount")
	val discount: Int,

	@field:SerializedName("promo_code")
	val promoCode: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("desc")
	val desc: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable

data class DataPagePromo(

	@field:SerializedName("totalItems")
	val totalItems: Int,

	@field:SerializedName("totalPages")
	val totalPages: Int,

	@field:SerializedName("currentPage")
	val currentPage: Int,

	@field:SerializedName("promos")
	val promos: List<DataPromo>
)
