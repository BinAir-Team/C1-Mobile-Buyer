package binar.finalproject.binair.buyer.data.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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