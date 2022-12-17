package binar.finalproject.binair.buyer.data.local

import androidx.room.*
import binar.finalproject.binair.buyer.data.model.DataWishList

@Dao
interface WishlistDAO {
    @Query("SELECT * FROM datawishlist ORDER BY id DESC")
    fun getWishList() : List<DataWishList>

    @Query("SELECT EXISTS(SELECT * FROM datawishlist WHERE id = :id)")
    fun isWishlisted(id: String) : Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWishList(wishlist: DataWishList)

    @Delete
    fun deleteWishList(note: DataWishList) : Int

    @Update
    fun updateWishList(note: DataWishList)
}