package binar.finalproject.binair.buyer.data.room

import android.content.Context
import android.provider.ContactsContract.Data
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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