package binar.finalproject.binair.buyer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import binar.finalproject.binair.buyer.data.WishListRepository
import binar.finalproject.binair.buyer.data.local.DataWishList
import binar.finalproject.binair.buyer.data.local.WishListDatabase
import kotlinx.coroutines.launch

class WishListViewModel(application: Application) : AndroidViewModel(application) {
    private var wishlistRepo : WishListRepository

    init {
        val wishlistDao = WishListDatabase.getInstance(application)?.wishlistDAO()
        wishlistRepo = WishListRepository(wishlistDao!!)
    }
    fun getDataNotes() : LiveData<List<DataWishList>> = wishlistRepo.getAllDataWishList()

    fun addNote(notes: DataWishList){
        viewModelScope.launch {
            wishlistRepo.addWishList(notes)
        }
    }
}