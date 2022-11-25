package binar.finalproject.binair.buyer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import binar.finalproject.binair.buyer.data.remote.APIService
import binar.finalproject.binair.buyer.data.response.TicketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(private val client : APIService) : ViewModel() {
    private val _allTicket = MutableLiveData<List<TicketItem>?>()
    val allTicket : LiveData<List<TicketItem>?> = _allTicket

    fun callGetAllTicket() {
        client.getAllTicket().enqueue(object : retrofit2.Callback<List<TicketItem>> {
            override fun onResponse(call: retrofit2.Call<List<TicketItem>>, response: retrofit2.Response<List<TicketItem>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _allTicket.postValue(data)
                    }else{
                        _allTicket.postValue(null)
                    }
                }else{
                    Log.e("Error : ", "onFailed: ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<TicketItem>>, t: Throwable) {
                Log.e("Error : ", "onFailure: ${t.message}")
            }
        })
    }
}
