package binar.finalproject.binair.buyer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import binar.finalproject.binair.buyer.data.model.DataWishList

@Database(entities = [DataWishList::class], version = 1 )
abstract class WishListDatabase : RoomDatabase () {
    abstract fun wishlistDAO() : WishlistDAO
    companion object{
        @Volatile
        private var INSTANCE : WishListDatabase? = null

        fun getInstance(context : Context): WishListDatabase? {
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        WishListDatabase::class.java,
                        "wishlist.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}