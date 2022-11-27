package binar.finalproject.binair.buyer.data.room

import android.provider.ContactsContract.Data
import androidx.lifecycle.LiveData

class WishListRepository (private val data : WishlistDAO){
    fun getAllDataWishList() : LiveData<List<DataWishList>> {
        return data.getWishList()
    }

    suspend fun addWishList(notes: DataWishList) = data.insertWishList(notes)

    suspend fun editWishList(notes: DataWishList) = data.updateWishList(notes)

    suspend fun deleteWishList(notes: DataWishList) = data.deleteWishList(notes)

}