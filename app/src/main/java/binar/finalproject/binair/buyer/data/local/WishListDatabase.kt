package binar.finalproject.binair.buyer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import binar.finalproject.binair.buyer.data.model.DataWishList

@Database(entities = [DataWishList::class], version = 3 )
abstract class WishListDatabase : RoomDatabase () {
    abstract fun wishlistDAO() : WishlistDAO
}