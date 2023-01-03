package binar.finalproject.binair.buyer.data.local

import androidx.room.*
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.data.response.CityAirport
import binar.finalproject.binair.buyer.data.response.DataPromo

@Dao
interface WishlistDAO {
    @Query("SELECT * FROM DataWishList WHERE user = :user ORDER BY id DESC")
    fun getWishList(user : String) : List<DataWishList>

    @Query("SELECT EXISTS(SELECT * FROM DataWishList WHERE id = :id AND user = :user)")
    fun isWishlisted(id: String, user: String) : Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWishList(wishlist: DataWishList)

    @Query("DELETE FROM DataWishList WHERE id = :id")
    fun deleteWishList(id: String)

    @Delete
    fun delWishList(wishlist: DataWishList)

    @Update
    fun updateWishList(wishlist: DataWishList)

    @Query("SELECT * FROM CityAirport")
    fun getCityAirport() : List<CityAirport>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCityAirport(listAirport : List<CityAirport>)

    @Query("SELECT * FROM DataPromo")
    fun getAllPromo() : List<DataPromo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPromo(listPromo : List<DataPromo>)
}