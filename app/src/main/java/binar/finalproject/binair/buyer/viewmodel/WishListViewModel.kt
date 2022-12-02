package binar.finalproject.binair.buyer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import binar.finalproject.binair.buyer.data.WishListRepository
import binar.finalproject.binair.buyer.data.local.WishListDatabase
import binar.finalproject.binair.buyer.data.model.DataWishList
import kotlinx.coroutines.launch

class WishListViewModel(application: Application) : AndroidViewModel(application) {
    private var wishlistRepo : WishListRepository

    init {
        val wishlistDao = WishListDatabase.getInstance(application)?.wishlistDAO()
        wishlistRepo = WishListRepository(wishlistDao!!)
    }
    fun getDataWishlist() : LiveData<List<DataWishList>> = wishlistRepo.getAllDataWishList()

    fun addWishlist(data: DataWishList){
        viewModelScope.launch {
            wishlistRepo.addWishList(data)
        }
    }
}