package binar.finalproject.binair.buyer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.data.response.CityAirport
import binar.finalproject.binair.buyer.data.response.DataPromo

@Database(entities = [DataWishList::class, CityAirport::class, DataPromo::class], version = 5 )
abstract class WishListDatabase : RoomDatabase () {
    abstract fun wishlistDAO() : WishlistDAO
}