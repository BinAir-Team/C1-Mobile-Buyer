package binar.finalproject.binair.buyer.data

import androidx.lifecycle.LiveData
import binar.finalproject.binair.buyer.data.model.DataWishList
import binar.finalproject.binair.buyer.data.local.WishlistDAO

class WishListRepository (private val data : WishlistDAO){
    fun getAllDataWishList() : LiveData<List<DataWishList>> {
        return data.getWishList()
    }

    suspend fun addWishList(notes: DataWishList) = data.insertWishList(notes)

    suspend fun editWishList(notes: DataWishList) = data.updateWishList(notes)

    suspend fun deleteWishList(notes: DataWishList) = data.deleteWishList(notes)

}