package binar.finalproject.binair.buyer.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WishlistDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWishList(wishlist: DataWishList)

    @Query(" SELECT * FROM datawishlist ORDER BY id DESC")
    fun getWishList() : LiveData<List<DataWishList>>

    @Delete
    fun deleteWishList(note: DataWishList) : Int

    @Update
    fun updateWishList(note: DataWishList)
}