package binar.finalproject.binair.buyer.data.local

import androidx.room.*
import binar.finalproject.binair.buyer.data.model.DataWishList

@Dao
interface WishlistDAO {
    @Query("SELECT * FROM datawishlist WHERE user = :user ORDER BY id DESC")
    fun getWishList(user : String) : List<DataWishList>

    @Query("SELECT EXISTS(SELECT * FROM datawishlist WHERE id = :id AND user = :user)")
    fun isWishlisted(id: String, user : String) : Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWishList(wishlist: DataWishList)

    @Delete
    fun deleteWishList(note: DataWishList) : Int

    @Update
    fun updateWishList(note: DataWishList)
}