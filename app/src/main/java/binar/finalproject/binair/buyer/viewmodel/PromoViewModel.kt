package binar.finalproject.binair.buyer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.data.PromoRepositoy
import binar.finalproject.binair.buyer.data.response.AllPromoResponse
import binar.finalproject.binair.buyer.data.response.DataPromo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PromoViewModel @Inject constructor(private var promoRepo : PromoRepositoy) : ViewModel(){
    fun getallticket() : LiveData<List<DataPromo>?> = promoRepo.callGetAllPromo()
}